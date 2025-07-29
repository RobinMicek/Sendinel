package cz.promptly.api.dto.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateBasicsResponseDto {
    private UUID id;
    private String name;
    private String description;
}
