package cz.promtply.backend.service;

import cz.promtply.backend.dto.email.EmailRequestDto;
import cz.promtply.backend.entity.Client;
import cz.promtply.backend.entity.Email;
import cz.promtply.backend.models.email.EmailJobRequestModel;
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

    EmailJobRequestModel getJobRequestModel(Email email);
    File renderEmailToPDF(Email email);
}
