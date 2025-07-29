package cz.promptly.worker;

import cz.promptly.worker.config.AppConfig;
import cz.promptly.worker.rabbitmq.EmailStatusProducer;
import cz.promptly.shared.config.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class WorkerApplication {

	Logger logger = LoggerFactory.getLogger(WorkerApplication.class);

	private final AppConfig appConfig;
	private final EmailStatusProducer emailStatusProducer;

	@Bean
	public CommandLineRunner runScalingWorkerPool(ConnectionFactory factory) {
		logger.info(Constants.LOG_BLOCK_DELIMITER);
		logger.info("Running ScalingWorkerPool");
		logger.info("Scaling monitor interval: {}", appConfig.getScalingMonitorInterval());
		logger.info("Max threads: {}", appConfig.getMaxThreads());
		logger.info("Worker idle wait: {}", appConfig.getWorkerIdleWait());
		logger.info("Worker idle timeout: {}", appConfig.getWorkerIdleTimeout());

		return args -> {
			ScalingManager manager = new ScalingManager(factory, Constants.RABBIT_MQ_JOB_REQUEST_QUEUE_NAME, appConfig, emailStatusProducer);
			manager.start();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(WorkerApplication.class, args);
	}

}
