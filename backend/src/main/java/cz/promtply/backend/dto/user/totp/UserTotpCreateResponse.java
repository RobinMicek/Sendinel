package cz.promtply.backend.dto.user.totp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTotpCreateResponse {
    private String secret;
    private String qrBase64;
}
