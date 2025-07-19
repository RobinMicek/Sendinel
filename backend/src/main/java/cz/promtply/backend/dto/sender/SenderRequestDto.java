package cz.promtply.backend.dto.sender;

import com.fasterxml.jackson.databind.JsonNode;
import cz.promtply.backend.dto.constraint.EnumValue;
import cz.promtply.backend.dto.constraint.SenderConfiguration;
import cz.promtply.backend.enums.SenderTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SenderConfiguration
public class SenderRequestDto {
    @NotBlank
    private String name;

    @EnumValue(enumClass = SenderTypes.class)
    private SenderTypes type;

    @NotNull
    private String description;

    @NotNull
    private JsonNode configuration;
}
