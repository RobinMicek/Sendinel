package cz.promptly.worker.senders;

import cz.promptly.worker.rabbitmq.EmailStatusProducer;
import cz.promtply.shared.enums.EmailStatusesEnum;
import cz.promtply.shared.models.email.EmailJobRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public abstract class BaseEmailSender implements Runnable {

    Logger logger = LoggerFactory.getLogger(BaseEmailSender.class);

    protected final EmailJobRequest jobRequest;
    protected final EmailStatusProducer statusProducer;

    protected abstract EmailStatusesEnum send(); // core sending logic

    protected void onSuccess(EmailStatusesEnum status) {
        logger.info("[EMAIL ID: {}] Job request exited without exception [STATUS: {}]", jobRequest.getEmailId(), status);
        statusProducer.sendEmailJobResponse(jobRequest.getEmailId(), status);
    }

    protected void onFail(Exception e) {
        logger.info("[EMAIL ID: {}] Job request failed: {}", jobRequest.getEmailId(), e.getMessage());
        statusProducer.sendEmailJobResponse(jobRequest.getEmailId(), EmailStatusesEnum.FAILED, e.getMessage());
    }

    @Override
    public final void run() {
        try {
            EmailStatusesEnum status = send();
            onSuccess(status);
        } catch (Exception e) {
            onFail(e);
        }
    }
}
