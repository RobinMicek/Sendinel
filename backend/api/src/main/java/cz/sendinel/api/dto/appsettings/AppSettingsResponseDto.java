package cz.sendinel.api.dto.appsettings;

import cz.sendinel.api.dto.user.UserBasicsResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppSettingsResponseDto {
    private boolean trackOpenedEmails;
    private boolean allowTemplateImports;
    private boolean displayNewVersionAlert;
    private boolean useGravatar;
    private UserBasicsResponseDto updatedBy;
    private Instant updatedOn;
}
