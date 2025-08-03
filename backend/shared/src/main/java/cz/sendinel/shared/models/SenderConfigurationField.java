package cz.sendinel.shared.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SenderConfigurationField {

    private final boolean required;
    private final Class<?> type;

    /**
     * If the value should be obfuscated when returned from the
     * server.
     * Only works with required=false and type=String.class.
     */
    private final boolean isSensitive;

}
