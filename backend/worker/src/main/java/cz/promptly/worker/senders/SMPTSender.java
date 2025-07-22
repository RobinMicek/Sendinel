package cz.promptly.worker.senders;

import com.fasterxml.jackson.databind.JsonNode;
import cz.promptly.worker.rabbitmq.EmailStatusProducer;
import cz.promtply.shared.enums.EmailStatusesEnum;
import cz.promtply.shared.models.email.EmailJobRequest;
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
    public EmailStatusesEnum send() {
        try {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

            JsonNode config = jobRequest.getSenderConfiguration();

            String host = config.path("smtpHost").asText(null);
            int port = config.path("smtpPort").asInt(-1);
            String from = config.path("fromAddress").asText(null);
            String username = config.path("username").asText(null);
            String password = config.path("password").asText(null);
            boolean startTls = config.path("startTls").asBoolean(true);

            if (host == null || port == -1 || from == null) {
                return EmailStatusesEnum.INVALID_CONFIGURATION;
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
            return EmailStatusesEnum.SENT;

        } catch (MailAuthenticationException e) {
            return EmailStatusesEnum.UNAUTHORIZED;
        } catch (MailSendException e) {
            return EmailStatusesEnum.BOUNCED;
        } catch (MessagingException e) {
            return EmailStatusesEnum.FAILED;
        } catch (Exception e) {
            return EmailStatusesEnum.FAILED;
        }
    }
}
