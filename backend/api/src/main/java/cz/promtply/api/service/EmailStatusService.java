package cz.promtply.api.service;

import cz.promtply.api.entity.Email;
import cz.promtply.api.entity.EmailStatus;
import cz.promtply.shared.enums.EmailStatusesEnum;

import java.util.List;

public interface EmailStatusService {
    List<EmailStatus> getByEmail(Email email);
    EmailStatus createStatus(EmailStatus emailStatus);
    EmailStatus createStatus(EmailStatusesEnum newStatus, Email email);
    EmailStatus createStatus(EmailStatusesEnum newStatus, String note, Email email);
}
