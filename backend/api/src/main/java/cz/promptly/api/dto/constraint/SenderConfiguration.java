package cz.promptly.api.dto.constraint;

import cz.promptly.api.dto.constraint.validator.SenderConfigurationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = SenderConfigurationValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SenderConfiguration {

    String message() default "Invalid configuration for the selected sender type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}