package cz.sendinel.api.service;

import cz.sendinel.api.dto.email.EmailRequestDto;
import cz.sendinel.api.entity.Client;
import cz.sendinel.api.entity.Email;
import cz.sendinel.api.entity.Template;
import cz.sendinel.api.exceptions.ResourceNotFoundException;
import cz.sendinel.api.exceptions.SchemaDoesNotMatchException;
import cz.sendinel.api.repository.EmailRepository;
import cz.sendinel.api.util.EmailRenderUtil;
import cz.sendinel.api.util.JsonSchemaValidator;
import cz.sendinel.api.util.TokenUtil;
import cz.sendinel.shared.config.Constants;
import cz.sendinel.shared.enums.EmailPrioritiesEnum;
import cz.sendinel.shared.enums.EmailStatusesEnum;
import cz.sendinel.shared.models.email.EmailJobRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final RabbitTemplate rabbitTemplate;
    private final TemplateService templateService;
    private final JsonSchemaValidator jsonSchemaValidator;
    private final AppSettingsService appSettingsService;

    @Value("${app.base-url}")
    private String appBaseUrl;

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
    public Page<Email> getEmails(Pageable pageable, Specification<Email> specification) {
        return emailRepository.findAll(specification, pageable);
    }


    @Override
    public void trackEmailOpened(String trackCode) {
        getEmailByTrackCode(trackCode).ifPresent(
                email -> emailStatusService.createStatus(EmailStatusesEnum.OPENED, email)
        );
    }

    @Override
    public EmailJobRequest getJobRequestModel(Email email) {
        EmailJobRequest emailJobRequest = new EmailJobRequest();

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

            // Add tracking image
            String htmlBody = email.getTemplate().getHtmlRaw();
            if (appSettingsService.getAppSettings().isTrackOpenedEmails()) {
                if (htmlBody.contains("</body>")) {
                    htmlBody =  htmlBody.replace("</body>", generateTrackingHtmlImage(email) + "</body>");
                } else {
                    htmlBody = htmlBody.concat(generateTrackingHtmlImage(email));
                }
            }

            emailJobRequest.setRenderedHtmlBody(
                EmailRenderUtil.renderTemplate(
                    htmlBody,
                    email.getTemplateVariables()
                )
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to render template", e);
        }

        return emailJobRequest;
    }

    @Transactional
    @Override
    public Email createEmailFromDtoAndSendJob(EmailRequestDto emailRequestDto, Client requestedBy) {
        Email email = createEmailFromDto(emailRequestDto, requestedBy);

        // Create status ENQUEUED, needs to be before sending the email job,
        // because unlike actually sending the job this can be reverted
        // (using transaction for this method)
        emailStatusService.createStatus(EmailStatusesEnum.ENQUEUED, email);

        // Sent email job to queue
        rabbitTemplate.convertAndSend(
                Constants.RABBIT_MQ_JOB_EXCHANGE_NAME,
                Constants.RABBIT_MQ_JOB_REQUEST_ROUTING_KEY,
                getJobRequestModel(email)
        );

        return email;
    }

    @Transactional
    @Override
    public Email resendEmail(Email email) {
        // Create statuses RESEND_REQUESTED and ENQUEUED, needs to be before sending the email job,
        // because unlike actually sending the job this can be reverted
        // (using transaction for this method)
        emailStatusService.createStatus(EmailStatusesEnum.RESEND_REQUESTED, email);
        emailStatusService.createStatus(EmailStatusesEnum.ENQUEUED, email);

        // Sent email job to queue
        rabbitTemplate.convertAndSend(
                Constants.RABBIT_MQ_JOB_EXCHANGE_NAME,
                Constants.RABBIT_MQ_JOB_REQUEST_ROUTING_KEY,
                getJobRequestModel(email)
        );

        return email;
    }

    @Override
    public File renderEmailToPDF(Email email) {
        // Add <pre> tag - rendering requires valid html
        String textToRender = "<pre>" + email.getTemplate().getTextRaw() + "</pre>";

        // Try to render HTML template, but use text template as fallback if no HTML is present
        if (email.getTemplate().getHtmlRaw() != null && !email.getTemplate().getHtmlRaw().isEmpty()) {
            textToRender = email.getTemplate().getHtmlRaw();

            // Add html tag if missing - rendering requires valid html
            // I know that this is a dirty way to add it, but it works
            if (!textToRender.contains("<html>")) {
                textToRender =  "<html>" + textToRender;
            }
            if (!textToRender.contains("</html>")) {
                textToRender = textToRender + "</html>";
            }
        }

        try {
            String renderedTemplate = EmailRenderUtil.renderTemplate(textToRender, email.getTemplateVariables());

            return EmailRenderUtil.renderHTMLtoPDF(renderedTemplate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to render PDF", e);
        }
    }

    private String generateTrackingHtmlImage(Email email) {
        String url = String.format("%s/%s/email/open?trackCode=%s", appBaseUrl, Constants.TRACKING_API_ROUTE_PREFIX.replace("/", ""), email.getTrackCode());

        return String.format("<img src=\"%s\" />", url);
    }

}
