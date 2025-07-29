package cz.promptly.api.model.templateexport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateExportTemplateData {
    private UUID id;
    private String name;
    private String description;
    private String subject;
    private String schema;
    private String textRaw;
    private String htmlRaw;
    private Instant createdOn;
    private Instant updatedOn;
    private Instant deletedOn;
}
