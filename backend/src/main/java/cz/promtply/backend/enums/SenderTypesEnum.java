package cz.promtply.backend.enums;

import cz.promtply.backend.models.SenderConfigurationField;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum SenderTypesEnum {
    SMTP(Map.of(
            "smtpHost", new SenderConfigurationField(true, String.class, false),
            "smtpPort", new SenderConfigurationField(true, Integer.class, false),
            "username", new SenderConfigurationField(false, String.class, false),
            "password", new SenderConfigurationField(false, String.class, true),
            "fromAddress", new SenderConfigurationField(true, String.class, false)
    ));

    private final Map<String, SenderConfigurationField> configurationSchema;
}
