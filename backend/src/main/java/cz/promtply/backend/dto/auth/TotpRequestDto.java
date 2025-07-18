package cz.promtply.backend.dto.auth;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotpRequestDto {
    @Pattern(regexp = "^\\d{6}$", message = "TOTP code must be exactly 6 digits")
    private String code;
}
