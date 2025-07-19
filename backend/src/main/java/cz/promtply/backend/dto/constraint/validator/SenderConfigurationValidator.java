package cz.promtply.backend.dto.constraint.validator;

import com.fasterxml.jackson.databind.JsonNode;
import cz.promtply.backend.dto.constraint.SenderConfiguration;
import cz.promtply.backend.enums.SenderTypes;
import cz.promtply.backend.models.SenderConfigurationField;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;
import java.util.Map;

// Annotated object need to have fields "type" and "configuration"
public class SenderConfigurationValidator implements ConstraintValidator<SenderConfiguration, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            SenderTypes type = (SenderTypes) getProperty(obj, "type");
            JsonNode config = (JsonNode) getProperty(obj, "configuration");

            if (type == null || config == null) {
                return true; // Let other annotations handle nulls
            }

            Map<String, SenderConfigurationField> schema = type.getConfigurationSchema();
            boolean valid = true;

            for (Map.Entry<String, SenderConfigurationField> entry : schema.entrySet()) {
                String key = entry.getKey();
                SenderConfigurationField field = entry.getValue();

                boolean hasKey = config.has(key);
                JsonNode value = hasKey ? config.get(key) : null;
                boolean isEmpty = value == null || value.isNull() || (value.isTextual() && value.asText().isBlank());

                if (field.isRequired()) {
                    if (!hasKey || isEmpty) {
                        addViolation(context, key, "is required and must not be empty");
                        valid = false;
                        continue;
                    }
                } else {
                    if (!hasKey) {
                        addViolation(context, key, "value is optional but must be present");
                        valid = false;
                        continue;
                    }
                }

                if (!isEmpty && !matchesType(value, field.getType())) {
                    addViolation(context, key, "has invalid type, expected " + field.getType().getSimpleName());
                    valid = false;
                }
            }

            return valid;

        } catch (Exception e) {
            return false;
        }
    }


    private boolean matchesType(JsonNode node, Class<?> type) {
        if (type == String.class) {
            return node.isTextual();
        } else if (type == Integer.class) {
            return node.isInt() || node.isLong();
        } else if (type == Boolean.class) {
            return node.isBoolean();
        } else if (type == Double.class || type == Float.class) {
            return node.isDouble() || node.isFloat() || node.isInt() || node.isLong();
        }

        return false;
    }

    private void addViolation(ConstraintValidatorContext context, String key, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Field '" + key + "' " + message)
                .addPropertyNode("configuration")
                .addConstraintViolation();
    }

    private Object getProperty(Object obj, String fieldName) throws Exception {
        Method getter = obj.getClass().getMethod("get" + capitalize(fieldName));
        return getter.invoke(obj);
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}