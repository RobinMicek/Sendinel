package cz.sendinel.api.dto.template.export;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateExportRequestDto {
    @NotNull
    private List<UUID> ids;

    @NotNull
    private boolean overwriteExisting;
}
