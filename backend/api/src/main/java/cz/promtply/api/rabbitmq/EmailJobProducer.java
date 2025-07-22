package cz.promtply.api.rabbitmq;

import cz.promtply.api.entity.Email;
import cz.promtply.api.service.EmailService;
import cz.promtply.api.service.EmailStatusService;
import cz.promtply.shared.config.Constants;
import cz.promtply.shared.enums.EmailStatusesEnum;
import cz.promtply.shared.models.email.EmailJobRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor()
public class EmailJobProducer {

    private final RabbitTemplate rabbitTemplate;
    private final EmailService emailService;
    private final EmailStatusService emailStatusService;

    public void sendEmailJobRequest(Email email) {
        EmailJobRequest emailJobRequest = emailService.getJobRequestModel(email);

        rabbitTemplate.convertAndSend(
                Constants.RABBIT_MQ_JOB_EXCHANGE_NAME,
                Constants.RABBIT_MQ_JOB_REQUEST_ROUTING_KEY,
                emailJobRequest
        );

        // Create status ENQUEUED only if sending succeeds
        emailStatusService.createStatus(EmailStatusesEnum.ENQUEUED, email);
    }
}