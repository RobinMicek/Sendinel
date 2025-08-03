package cz.sendinel.api.dto.appsettings;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppSettingsRequestDto {
    @NotNull
    private boolean trackOpenedEmails;

    @NotNull
    private boolean allowTemplateImports;

    @NotNull
    private boolean displayNewVersionAlert;

    @NotNull
    private boolean useGravatar;
}
