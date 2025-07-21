package cz.promtply.api.dto.user.totp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTotpStatusResponseDto {
    private boolean exists;
    private boolean activated;
}
