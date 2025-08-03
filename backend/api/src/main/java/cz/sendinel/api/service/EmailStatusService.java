package cz.sendinel.api.service;

import cz.sendinel.api.entity.Email;
import cz.sendinel.api.entity.EmailStatus;
import cz.sendinel.shared.enums.EmailStatusesEnum;

import java.util.List;

public interface EmailStatusService {
    List<EmailStatus> getByEmail(Email email);
    EmailStatus createStatus(EmailStatus emailStatus);
    EmailStatus createStatus(EmailStatusesEnum newStatus, Email email);
    EmailStatus createStatus(EmailStatusesEnum newStatus, String note, Email email);
}
