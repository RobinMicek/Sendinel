package cz.promtply.api.rabbitmq;

import cz.promtply.api.entity.Email;
import cz.promtply.api.service.EmailService;
import cz.promtply.api.service.EmailStatusService;
import cz.promtply.shared.config.Constants;
import cz.promtply.shared.models.email.EmailJobResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailJobStatusReceiver {

    private final EmailService emailService;
    private final EmailStatusService emailStatusService;

    @RabbitListener(queues = Constants.RABBIT_MQ_JOB_RESPONSE_QUEUE_NAME)
    public void receive(EmailJobResponse emailJobResponse) {
        Email email = emailService.getEmailById(emailJobResponse.getEmailId()).orElse(null);

        emailStatusService.createStatus(emailJobResponse.getStatus(), emailJobResponse.getNote(), email);
    }

}
