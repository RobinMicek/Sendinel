package cz.promptly.api.service;

import cz.promptly.api.entity.Email;
import cz.promptly.api.entity.EmailStatus;
import cz.promptly.shared.enums.EmailStatusesEnum;

import java.util.List;

public interface EmailStatusService {
    List<EmailStatus> getByEmail(Email email);
    EmailStatus createStatus(EmailStatus emailStatus);
    EmailStatus createStatus(EmailStatusesEnum newStatus, Email email);
    EmailStatus createStatus(EmailStatusesEnum newStatus, String note, Email email);
}
