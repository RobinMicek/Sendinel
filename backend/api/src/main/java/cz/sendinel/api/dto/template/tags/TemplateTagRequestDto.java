package cz.sendinel.api.dto.template.tags;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateTagRequestDto {
    @NotNull
    private String name;
}
