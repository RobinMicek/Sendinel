package cz.sendinel.worker.senders;

import cz.sendinel.shared.enums.EmailStatusesEnum;
import cz.sendinel.shared.models.email.EmailJobRequest;
import cz.sendinel.shared.models.email.EmailJobResponse;
import cz.sendinel.worker.rabbitmq.EmailStatusProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public abstract class BaseEmailSender implements Runnable {

    Logger logger = LoggerFactory.getLogger(BaseEmailSender.class);

    protected final EmailJobRequest jobRequest;
    protected final EmailStatusProducer statusProducer;

    protected abstract EmailJobResponse send(); // core sending logic

    protected void onSuccess(EmailJobResponse emailJobResponse) {
        logger.info("[EMAIL ID: {}] Job request processed [STATUS: {}, NOTE: {}]", emailJobResponse.getEmailId(), emailJobResponse.getStatus(), emailJobResponse.getNote());
        statusProducer.sendEmailJobResponse(emailJobResponse);
    }

    protected void onFail(Exception e) {
        logger.error("[EMAIL ID: {}] Job request failed: {}", jobRequest.getEmailId(), e.getMessage());
        statusProducer.sendEmailJobResponse(new EmailJobResponse(jobRequest.getEmailId(), EmailStatusesEnum.FAILED, e.getMessage()));
    }

    @Override
    public final void run() {
        try {
            EmailJobResponse status = send();
            onSuccess(status);
        } catch (Exception e) {
            onFail(e);
        }
    }
}
