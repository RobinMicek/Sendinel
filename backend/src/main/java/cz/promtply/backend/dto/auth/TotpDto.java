package cz.promtply.backend.dto.auth;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotpDto {
    @Min(value = 100000, message = "TOTP code must be a 6-digit number")
    @Max(value = 999999, message = "TOTP code must be a 6-digit number")
    private int code;
}
