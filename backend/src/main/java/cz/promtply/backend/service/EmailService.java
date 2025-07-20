package cz.promtply.backend.service;

import cz.promtply.backend.dto.email.EmailRequestDto;
import cz.promtply.backend.entity.Client;
import cz.promtply.backend.entity.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EmailService {
    Optional<Email> getEmailById(UUID id);
    Page<Email> getEmails(Pageable pageable);
    Email createEmail(Email email);
    Email createEmailFromDto(EmailRequestDto emailRequestDto, Client createdBy);
}
