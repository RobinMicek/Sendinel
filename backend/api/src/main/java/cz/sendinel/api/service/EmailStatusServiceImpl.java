package cz.sendinel.api.service;

import cz.sendinel.api.entity.Email;
import cz.sendinel.api.entity.EmailStatus;
import cz.sendinel.api.repository.EmailStatusRepository;
import cz.sendinel.shared.enums.EmailStatusesEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailStatusServiceImpl implements EmailStatusService {

    private final EmailStatusRepository emailStatusRepository;

    @Override
    public List<EmailStatus> getByEmail(Email email) {
        return emailStatusRepository.findByEmailId(email.getId());
    }

    @Override
    public EmailStatus createStatus(EmailStatus emailStatus) {
        emailStatus.setCreatedOn(Instant.now());

        return emailStatusRepository.save(emailStatus);
    }

    @Override
    public EmailStatus createStatus(EmailStatusesEnum newStatus, Email email) {
        EmailStatus emailStatus = new EmailStatus();
        emailStatus.setStatus(newStatus);
        emailStatus.setEmail(email);

        return createStatus(emailStatus);
    }

    @Override
    public EmailStatus createStatus(EmailStatusesEnum newStatus, String note, Email email) {
        EmailStatus emailStatus = new EmailStatus();
        emailStatus.setStatus(newStatus);
        emailStatus.setNote(note);
        emailStatus.setEmail(email);

        return createStatus(emailStatus);
    }

}
