package cz.promtply.backend.models.templateexport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateExportMetaData {
    private Instant exportedOn;
    private String appVersion;
    private String dbVersion;
    private boolean overwriteExisting;
}
