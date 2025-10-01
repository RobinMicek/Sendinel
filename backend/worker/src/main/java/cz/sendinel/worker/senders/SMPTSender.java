package cz.sendinel.worker.senders;

import com.fasterxml.jackson.databind.JsonNode;
import cz.sendinel.shared.enums.EmailStatusesEnum;
import cz.sendinel.shared.models.email.EmailJobRequest;
import cz.sendinel.shared.models.email.EmailJobResponse;
import cz.sendinel.worker.rabbitmq.EmailStatusProducer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Properties;

public class SMPTSender extends BaseEmailSender {

    public SMPTSender(EmailJobRequest jobRequest, EmailStatusProducer statusProducer) {
        super(jobRequest, statusProducer);
    }

    @Override
    public EmailJobResponse send() {
        try {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

            JsonNode config = jobRequest.getSenderConfiguration();

            String host = config.path("smtpHost").asText(null);
            int port = config.path("smtpPort").asInt(-1);
            String from = config.path("fromAddress").asText(null);
            String username = config.path("username").asText(null);
            String password = config.path("password").asText(null);
            boolean startTls = config.path("startTls").asBoolean(true);
            boolean ssl = config.path("ssl").asBoolean(true);
            int timeout =  config.path("timeout").asInt(5000); // Default 5 ms

            if (host == null || port == -1 || from == null) {
                return new EmailJobResponse(jobRequest.getEmailId(), EmailStatusesEnum.INVALID_CONFIGURATION, null);
            }

            mailSender.setHost(host);
            mailSender.setPort(port);
            if (username != null && password != null) {
                mailSender.setUsername(username);
                mailSender.setPassword(password);
            }

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", username != null);
            props.put("mail.smtp.starttls.enable", String.valueOf(startTls));
            props.put("mail.smtp.ssl.enable", String.valueOf(ssl));

            // Add timeouts to prevent indefinite blocking
            props.put("mail.smtp.connectiontimeout", timeout);
            props.put("mail.smtp.timeout", timeout);
            props.put("mail.smtp.writetimeout", timeout);

            // Create email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(jobRequest.getToAddress());
            helper.setSubject(jobRequest.getSubject());

            if (jobRequest.getReplyTo() != null) {
                helper.setReplyTo(jobRequest.getReplyTo());
            }

            helper.setFrom(from);

            if (jobRequest.getRenderedHtmlBody() != null && !jobRequest.getRenderedHtmlBody().isEmpty()) {
                helper.setText(jobRequest.getRenderedTextBody(), jobRequest.getRenderedHtmlBody());
            } else {
                helper.setText(jobRequest.getRenderedTextBody(), false);
            }

            mailSender.send(message);
            return new EmailJobResponse(jobRequest.getEmailId(), EmailStatusesEnum.SENT, null);

        } catch (MailAuthenticationException e) {
            return new EmailJobResponse(jobRequest.getEmailId(), EmailStatusesEnum.UNAUTHORIZED, e.getMessage());
        } catch (MailSendException e) {
            return new EmailJobResponse(jobRequest.getEmailId(), EmailStatusesEnum.BOUNCED, e.getMessage());
        } catch (MessagingException e) {
            return new EmailJobResponse(jobRequest.getEmailId(), EmailStatusesEnum.FAILED, e.getMessage());
        } catch (Exception e) {
            return new EmailJobResponse(jobRequest.getEmailId(), EmailStatusesEnum.FAILED, e.getMessage());
        }
    }
}
