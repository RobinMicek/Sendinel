package cz.promtply.backend.service;

import cz.promtply.backend.dto.email.EmailRequestDto;
import cz.promtply.backend.entity.Client;
import cz.promtply.backend.entity.Email;
import cz.promtply.backend.entity.Template;
import cz.promtply.backend.enums.EmailPrioritiesEnum;
import cz.promtply.backend.enums.EmailStatusesEnum;
import cz.promtply.backend.exceptions.ResourceNotFoundException;
import cz.promtply.backend.exceptions.SchemaDoesNotMatchException;
import cz.promtply.backend.repository.EmailRepository;
import cz.promtply.backend.util.JsonSchemaValidator;
import cz.promtply.backend.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final EmailStatusService emailStatusService;
    private final TemplateService templateService;
    private final JsonSchemaValidator jsonSchemaValidator;

    @Override
    public Email createEmail(Email email) {
        email.setCreatedOn(Instant.now());

        return emailRepository.save(email);
    }

    @Override
    public Email createEmailFromDto(EmailRequestDto emailRequestDto, Client requestedBy) {
        if (requestedBy.getSender() == null) {
            throw new ResourceNotFoundException("No sender is configured for client id " + requestedBy.getId());
        }

        Template template = templateService.getTemplateById(emailRequestDto.getTemplate()).orElseThrow(
                () -> new ResourceNotFoundException("Template not found with id " + emailRequestDto.getTemplate())
        );

        // Validate templateVariables against the schema
        boolean isValidSchema = jsonSchemaValidator.validate(template.getSchema(), emailRequestDto.getTemplateVariables());
        if (!isValidSchema) {
            throw new SchemaDoesNotMatchException("Template variables do not match the schema for template id " + template.getId());
        }

        Email email = new Email();
        email.setToAddress(emailRequestDto.getToAddress());
        email.setTemplate(template);
        email.setTemplateVariables(emailRequestDto.getTemplateVariables());

        // Fallback if priority is not set
        email.setPriority(emailRequestDto.getPriority() != null ? emailRequestDto.getPriority() : EmailPrioritiesEnum.NORMAL);

        email.setRequestedBy(requestedBy);
        email.setSentBy(requestedBy.getSender());

        // Create tracking code
        email.setTrackCode(TokenUtil.generateToken());

        Email savedEmail = createEmail(email);

        // Create email status
        emailStatusService.createStatus(EmailStatusesEnum.CREATED, savedEmail);

        // Re-fetch saved email (now includes the CREATED status)
        return getEmailById(savedEmail.getId()).orElseThrow(() -> new ResourceNotFoundException("Unable to re-fetch the email with id " + savedEmail.getId()));
    }

    @Override
    public Optional<Email> getEmailById(UUID id) {
        return emailRepository.findById(id);
    }

    @Override
    public Page<Email> getEmails(Pageable pageable) {
        return emailRepository.findAll(pageable);
    }

}
