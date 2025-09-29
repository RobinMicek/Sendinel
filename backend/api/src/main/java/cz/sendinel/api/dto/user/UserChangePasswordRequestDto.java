package cz.sendinel.api.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChangePasswordRequestDto {
    @NotBlank
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;
}
