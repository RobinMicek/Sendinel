package cz.promtply.backend.service;

import cz.promtply.backend.entity.Email;
import cz.promtply.backend.entity.EmailStatus;
import cz.promtply.backend.enums.EmailStatuses;

import java.util.List;

public interface EmailStatusService {
    List<EmailStatus> getByEmail(Email email);
    EmailStatus createStatus(EmailStatuses newStatus, Email email);
}
