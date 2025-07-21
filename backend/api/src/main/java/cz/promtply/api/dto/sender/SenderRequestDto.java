package cz.promtply.api.dto.sender;

import com.fasterxml.jackson.databind.JsonNode;
import cz.promtply.api.dto.constraint.EnumValue;
import cz.promtply.api.dto.constraint.SenderConfiguration;
import cz.promtply.shared.enums.SenderTypesEnum;
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

    @EnumValue(enumClass = SenderTypesEnum.class)
    private SenderTypesEnum type;

    @NotNull
    private String description;

    @NotNull
    private JsonNode configuration;
}
