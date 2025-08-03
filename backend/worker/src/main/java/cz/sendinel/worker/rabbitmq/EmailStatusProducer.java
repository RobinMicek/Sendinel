package cz.sendinel.worker.rabbitmq;

import cz.sendinel.shared.config.Constants;
import cz.sendinel.shared.enums.EmailStatusesEnum;
import cz.sendinel.shared.models.email.EmailJobResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailStatusProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmailJobResponse(UUID emailId, EmailStatusesEnum status) {
        rabbitTemplate.convertAndSend(
                Constants.RABBIT_MQ_JOB_EXCHANGE_NAME,
                Constants.RABBIT_MQ_JOB_RESPONSE_ROUTING_KEY,
                new EmailJobResponse(emailId, status, null)
        );
    }

    public void sendEmailJobResponse(UUID emailId, EmailStatusesEnum status, String note) {
        rabbitTemplate.convertAndSend(
                Constants.RABBIT_MQ_JOB_EXCHANGE_NAME,
                Constants.RABBIT_MQ_JOB_RESPONSE_ROUTING_KEY,
                new EmailJobResponse(emailId, status, note)
        );
    }
}
