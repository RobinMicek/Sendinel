package cz.sendinel.api.service;

import cz.sendinel.api.dto.email.EmailRequestDto;
import cz.sendinel.api.entity.Client;
import cz.sendinel.api.entity.Email;
import cz.sendinel.shared.models.email.EmailJobRequest;
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

    EmailJobRequest getJobRequestModel(Email email);
    Email createEmailFromDtoAndSendJob(EmailRequestDto emailRequestDto, Client requestedBy);

    void trackEmailOpened(String trackCode);
    File renderEmailToPDF(Email email);
}
