package cz.promptly.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import cz.promptly.worker.rabbitmq.EmailStatusProducer;
import cz.promptly.worker.senders.BaseEmailSender;
import cz.promptly.worker.senders.SMPTSender;
import cz.promtply.shared.enums.SenderTypesEnum;
import cz.promtply.shared.models.email.EmailJobRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class EmailWorkerThread implements Runnable {
    Logger logger = LoggerFactory.getLogger(EmailWorkerThread.class);

    private final Channel channel;
    private final String queueName;
    private final EmailStatusProducer statusProducer;
    private final int jobMaxRetries;
    private final long idleWaitMillis;
    private final long idleTimeoutMillis;

    private final AtomicBoolean running = new AtomicBoolean(true);

    public void stop() {
        running.set(false);
    }

    @Override
    public void run() {
        long lastMessageTime = System.currentTimeMillis();

        while (running.get()) {
            try {
                GetResponse response = channel.basicGet(queueName, false);

                if (response == null) {
                    Thread.sleep(idleWaitMillis);
                    if (System.currentTimeMillis() - lastMessageTime > idleTimeoutMillis) {
                        stop();
                    }
                    continue;
                }

                long tag = response.getEnvelope().getDeliveryTag();
                byte[] body = response.getBody();

                EmailJobRequest job = new ObjectMapper().readValue(body, EmailJobRequest.class);
                logger.info("[EMAIL ID: {}] Job received", job.getEmailId());

                try {
                    BaseEmailSender worker = getSender(job, statusProducer);
                    worker.run();

                    channel.basicAck(tag, false);
                    logger.info("[EMAIL ID: {}] Job done & acknowledged", job.getEmailId());

                } catch (Exception jobError) {
                    handleJobFailure(job, tag, jobMaxRetries);
                }

                lastMessageTime = System.currentTimeMillis();

            } catch (Exception e) {
                logger.error("Unexpected error in job runner: {}", e.getMessage(), e);
            }
        }

        closeChannel();
    }

    private void closeChannel() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
                logger.info("Channel closed.");
            }
        } catch (Exception e) {
            logger.error("Error closing channel: {}", e.getMessage(), e);
        }
    }

    private void handleJobFailure(EmailJobRequest job, long tag, int maxRetries) {
        try {
            int retries = job.getRetryCount() + 1;
            job.setRetryCount(retries++);

            if (retries <= maxRetries) {
                logger.warn("Job failed, retrying (attempt {}/{}): {}", retries, maxRetries, job);

                byte[] updatedBody = new ObjectMapper().writeValueAsBytes(job);
                channel.basicReject(tag, false);  // reject original
                channel.basicPublish("", queueName, null, updatedBody);  // requeue with updated retry count
            } else {
                logger.error("Job failed after {} attempts, discarding: {}", maxRetries, job);
                channel.basicReject(tag, false);  // discard
            }

        } catch (Exception e) {
            logger.error("Error handling job failure: {}", e.getMessage(), e);
        }
    }



    private BaseEmailSender getSender(EmailJobRequest job, EmailStatusProducer statusProducer) {
        return switch (job.getSenderType()) {
            case SenderTypesEnum.SMTP -> new SMPTSender(job, statusProducer);

            case null -> throw new IllegalArgumentException("Sender type not set");
            default -> throw new IllegalArgumentException("Sender type not supported");
        };
    }

}
