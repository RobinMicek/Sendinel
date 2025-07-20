package cz.promtply.backend.dto.email;

import com.fasterxml.jackson.databind.JsonNode;
import cz.promtply.backend.dto.constraint.EnumValue;
import cz.promtply.backend.enums.EmailPrioritiesEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto {
    @NotBlank
    @Email
    private String toAddress;

    @NotNull
    private UUID template;

    @NotNull
    private JsonNode templateVariables;

    @EnumValue(enumClass = EmailPrioritiesEnum.class)
    private EmailPrioritiesEnum priority;
}
