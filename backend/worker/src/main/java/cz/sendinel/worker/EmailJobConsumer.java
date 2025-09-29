package cz.sendinel.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import cz.sendinel.shared.config.Constants;
import cz.sendinel.shared.enums.EmailStatusesEnum;
import cz.sendinel.shared.models.email.EmailJobRequest;
import cz.sendinel.shared.models.email.EmailJobResponse;
import cz.sendinel.worker.config.AppConfig;
import cz.sendinel.worker.rabbitmq.EmailStatusProducer;
import cz.sendinel.worker.senders.BaseEmailSender;
import cz.sendinel.worker.senders.SMPTSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailJobConsumer {

    Logger logger = LoggerFactory.getLogger(WorkerApplication.class);

    private final EmailStatusProducer statusProducer;
    private final AppConfig appConfig;

    @RabbitListener(
            queues = Constants.RABBIT_MQ_JOB_REQUEST_QUEUE_NAME,
            containerFactory = "rabbitListenerContainerFactory",
            ackMode = "MANUAL"
    )
    public void handleMessage(EmailJobRequest job, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG)  long deliveryTag) {
        try {
            BaseEmailSender sender = switch (job.getSenderType()) {
                case SMTP -> new SMPTSender(job, statusProducer);
                default -> throw new IllegalArgumentException("Unsupported sender type");
            };

            sender.run(); // process the job
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            logger.error("Job failed [EMAIL_ID: {}, RETRIES: {}/{}]: {}", job.getEmailId(), appConfig.getJobMaxRetries(), job.getRetryCount(), e.getMessage());

            // Send status response
            statusProducer.sendEmailJobResponse(
                    new EmailJobResponse(
                            job.getEmailId(),
                            EmailStatusesEnum.FAILED,
                            String.format("Failed to send email [RETRIES: %d/%d]: %s", job.getRetryCount(), appConfig.getJobMaxRetries(), e.getMessage()))
            );

            // Handle re-queueing
            handleJobFailure(job, channel, deliveryTag);
        }
    }

    private void handleJobFailure(EmailJobRequest job, Channel channel, long deliveryTag) {
        try {
            int retries = job.getRetryCount() + 1;
            job.setRetryCount(retries);

            if (retries <= appConfig.getJobMaxRetries()) {
                // requeue with incremented retry
                byte[] updatedBody = new ObjectMapper().writeValueAsBytes(job);
                channel.basicReject(deliveryTag, false); // reject original
                channel.basicPublish("", Constants.RABBIT_MQ_JOB_REQUEST_QUEUE_NAME, null, updatedBody);
            } else {
                channel.basicReject(deliveryTag, false); // discard
            }
        } catch (Exception ex) {
            logger.error("Failed to handle job {}: {}", job, ex.getMessage(), ex);
        }
    }
}

