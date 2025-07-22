package cz.promtply.api.service;

import cz.promtply.api.dto.email.EmailRequestDto;
import cz.promtply.api.entity.Client;
import cz.promtply.api.entity.Email;
import cz.promtply.shared.models.email.EmailJobRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

public interface EmailService {
    Optional<Email> getEmailById(UUID id);
    Optional<Email> getEmailByTrackCode(String trackCode);
    Page<Email> getEmails(Pageable pageable);
    Email createEmail(Email email);
    Email createEmailFromDto(EmailRequestDto emailRequestDto, Client createdBy);

    void trackEmailOpened(String trackCode);

    EmailJobRequest getJobRequestModel(Email email);
    File renderEmailToPDF(Email email);
}
