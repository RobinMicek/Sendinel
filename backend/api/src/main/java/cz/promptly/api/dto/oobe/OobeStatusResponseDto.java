package cz.promptly.api.dto.oobe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OobeStatusResponseDto {
    private boolean isOobe;
}
