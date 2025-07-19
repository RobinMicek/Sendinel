package cz.promtply.backend.enums;

import cz.promtply.backend.models.SenderConfigurationField;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum SenderTypes {
    SMTP(Map.of(
            "smtpHost", new SenderConfigurationField(true, String.class),
            "smtpPort", new SenderConfigurationField(true, Integer.class),
            "username", new SenderConfigurationField(false, String.class),
            "password", new SenderConfigurationField(false, String.class),
            "fromAddress", new SenderConfigurationField(true, String.class)
    ));

    private final Map<String, SenderConfigurationField> configurationSchema;
}
