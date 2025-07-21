package cz.promtply.api.service;

import cz.promtply.api.dto.email.EmailRequestDto;
import cz.promtply.api.entity.Client;
import cz.promtply.api.entity.Email;
import cz.promtply.api.entity.Template;
import cz.promtply.shared.enums.EmailPrioritiesEnum;
import cz.promtply.shared.enums.EmailStatusesEnum;
import cz.promtply.api.exceptions.ResourceNotFoundException;
import cz.promtply.api.exceptions.SchemaDoesNotMatchException;
import cz.promtply.shared.models.email.EmailJobRequestModel;
import cz.promtply.api.repository.EmailRepository;
import cz.promtply.api.util.EmailRenderUtil;
import cz.promtply.api.util.JsonSchemaValidator;
import cz.promtply.api.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
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
    public Optional<Email> getEmailByTrackCode(String trackCode) {
        return emailRepository.findByTrackCode(trackCode);
    }

    @Override
    public Page<Email> getEmails(Pageable pageable) {
        return emailRepository.findAll(pageable);
    }

    @Override
    public void trackEmailOpened(String trackCode) {
        getEmailByTrackCode(trackCode).ifPresent(
                email -> emailStatusService.createStatus(EmailStatusesEnum.OPENED, email)
        );
    }

    @Override
    public EmailJobRequestModel getJobRequestModel(Email email) {
        EmailJobRequestModel emailJobRequest = new EmailJobRequestModel();

        // Set info
        emailJobRequest.setEmailId(email.getId());
        emailJobRequest.setToAddress(email.getToAddress());
        emailJobRequest.setReplyTo(email.getTemplate().getReplyTo());
        emailJobRequest.setPriority(email.getPriority());
        emailJobRequest.setSenderType(email.getSentBy().getType());
        emailJobRequest.setSenderConfiguration(email.getSentBy().getConfiguration());

        // Render templates
        try {
            emailJobRequest.setSubject(
                EmailRenderUtil.renderTemplate(
                    email.getTemplate().getSubject(),
                    email.getTemplateVariables()
                )
            );

            emailJobRequest.setRenderedTextBody(
                EmailRenderUtil.renderTemplate(
                    email.getTemplate().getTextRaw(),
                    email.getTemplateVariables()
                )
            );

            emailJobRequest.setRenderedHtmlBody(
                EmailRenderUtil.renderTemplate(
                    email.getTemplate().getHtmlRaw(),
                    email.getTemplateVariables()
                )
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to render template", e);
        }

        return emailJobRequest;
    }

    @Override
    public File renderEmailToPDF(Email email) {
        String textToRender = email.getTemplate().getTextRaw();

        // Try to render HTML template, but use text template as fallback if no HTML is present
        if (email.getTemplate().getHtmlRaw() != null && !email.getTemplate().getHtmlRaw().isEmpty()) {
            textToRender = email.getTemplate().getHtmlRaw();
        }

        try {
            String renderedTemplate = EmailRenderUtil.renderTemplate(textToRender, email.getTemplateVariables());

            return EmailRenderUtil.renderHTMLtoPDF(renderedTemplate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to render PDF", e);
        }
    }

}
