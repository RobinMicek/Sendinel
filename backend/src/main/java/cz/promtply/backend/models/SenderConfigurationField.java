package cz.promtply.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SenderConfigurationField {

    private final boolean required;
    private final Class<?> type;

    public boolean isRequired() {
        return required;
    }

    public Class<?> getType() {
        return type;
    }
}
