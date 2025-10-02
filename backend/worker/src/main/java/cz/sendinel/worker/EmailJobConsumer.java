package cz.sendinel.worker;

import cz.sendinel.shared.config.Constants;
import cz.sendinel.shared.enums.EmailStatusesEnum;
import cz.sendinel.shared.models.email.EmailJobRequest;
import cz.sendinel.shared.models.email.EmailJobResponse;
import cz.sendinel.worker.rabbitmq.EmailStatusProducer;
import cz.sendinel.worker.senders.BaseEmailSender;
import cz.sendinel.worker.senders.SMPTSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailJobConsumer {

    Logger logger = LoggerFactory.getLogger(WorkerApplication.class);

    private final EmailStatusProducer statusProducer;

    @RabbitListener(
            queues = Constants.RABBIT_MQ_JOB_REQUEST_QUEUE_NAME,
            containerFactory = "rabbitListenerContainerFactory",
            ackMode = "AUTO"
    )
    public void handleMessage(EmailJobRequest job) {
        try {
            logger.info("[EMAIL_ID: {}] Received email job request", job.getEmailId());

            BaseEmailSender sender = switch (job.getSenderType()) {
                case SMTP -> new SMPTSender(job, statusProducer);
                default -> throw new IllegalArgumentException("Unsupported sender type");
            };

            sender.run(); // process the job

        } catch (Exception e) {
            logger.error("[EMAIL_ID: {}] Job failed: {}", job.getEmailId(), e.getMessage());

            // Send status response
            statusProducer.sendEmailJobResponse(
                    new EmailJobResponse(
                            job.getEmailId(),
                            EmailStatusesEnum.FAILED,
                            String.format("Failed to send email: %s", e.getMessage()))
            );
        }
    }
}

