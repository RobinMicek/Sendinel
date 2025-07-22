package cz.promptly.worker;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import cz.promptly.worker.config.AppConfig;
import cz.promptly.worker.rabbitmq.EmailStatusProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
public class ScalingManager {
    Logger logger = LoggerFactory.getLogger(ScalingManager.class);

    private final ConnectionFactory factory;
    private final String queueName;
    private final AppConfig appConfig;
    private final EmailStatusProducer statusProducer;

    private final ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService workers = Executors.newCachedThreadPool();
    private final List<Future<?>> activeWorkers = Collections.synchronizedList(new ArrayList<>());

    public void start() {
        // Spawn one worker and scale it from there
        spawnWorker();

        monitor.scheduleAtFixedRate(this::scale, 0, appConfig.getScalingMonitorInterval(), TimeUnit.MILLISECONDS);
    }

    private void spawnWorker() {
        try {
            Channel channel = factory.createConnection().createChannel(false);
            EmailWorkerThread worker = new EmailWorkerThread(channel, queueName, statusProducer, appConfig.getJobMaxRetries(), appConfig.getWorkerIdleWait(), appConfig.getWorkerIdleTimeout());
            Future<?> future = workers.submit(worker);
            activeWorkers.add(future);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private int getQueueSize() {
        try (Connection connection = factory.createConnection();
             Channel channel = connection.createChannel(false)) {
            AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queueName);
            return declareOk.getMessageCount();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return 0;
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void scale() {
        int queueSize = getQueueSize();
        int activeCount = getAliveWorkerCount();

        // Scale up if needed
        if (queueSize > activeCount && activeCount < appConfig.getMaxThreads()) {
            int toSpawn = Math.min(queueSize - activeCount, appConfig.getMaxThreads() - activeCount);
            for (int i = 0; i < toSpawn; i++) {
                spawnWorker();
            }
        }

        // Clean up completed workers
        activeWorkers.removeIf(f -> f.isDone() || f.isCancelled());
    }

    private int getAliveWorkerCount() {
        return (int) activeWorkers.stream().filter(f -> !f.isDone()).count();
    }
}
