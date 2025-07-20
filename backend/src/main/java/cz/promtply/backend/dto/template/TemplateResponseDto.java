package cz.promtply.backend.dto.template;

import cz.promtply.backend.dto.UpdatedByResponseDto;
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
    private String markdownRaw;
    private boolean preferMarkdown;
    private String replyTo;
    private UpdatedByResponseDto createdBy;
    private UpdatedByResponseDto updatedBy;
    private Instant createdOn;
    private Instant updatedOn;
    private Instant DeletedOn;

}
