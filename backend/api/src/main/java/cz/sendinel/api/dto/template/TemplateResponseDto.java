package cz.sendinel.api.dto.template;

import cz.sendinel.api.dto.user.UserBasicsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String subject;
    private String schema;
    private String textRaw;
    private String htmlRaw;
    private String replyTo;
    private UserBasicsResponseDto createdBy;
    private UserBasicsResponseDto updatedBy;
    private Instant createdOn;
    private Instant updatedOn;
    private Instant DeletedOn;
}
