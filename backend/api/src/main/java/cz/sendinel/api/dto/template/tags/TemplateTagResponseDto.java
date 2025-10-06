package cz.sendinel.api.dto.template.tags;

import cz.sendinel.api.dto.user.UserBasicsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateTagResponseDto {
    private UUID id;
    private String name;
    private Instant createdOn;
    private UserBasicsResponseDto createdBy;
}
