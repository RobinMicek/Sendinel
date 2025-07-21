package cz.promtply.api.service;

import cz.promtply.api.entity.Email;
import cz.promtply.api.entity.EmailStatus;
import cz.promtply.shared.enums.EmailStatusesEnum;
import cz.promtply.api.repository.EmailStatusRepository;
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
    public EmailStatus createStatus(EmailStatusesEnum newStatus, Email email) {
        EmailStatus emailStatus = new EmailStatus();
        emailStatus.setStatus(newStatus);
        emailStatus.setEmail(email);
        emailStatus.setCreatedOn(Instant.now());

        return emailStatusRepository.save(emailStatus);
    }
}
