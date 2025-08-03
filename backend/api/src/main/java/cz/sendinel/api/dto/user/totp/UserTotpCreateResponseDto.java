package cz.sendinel.api.dto.user.totp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTotpCreateResponseDto {
    private String secret;
    private String qrBase64;
}
