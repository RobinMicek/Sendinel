package cz.promtply.backend.dto.user;

import cz.promtply.backend.dto.constraint.EnumValue;
import cz.promtply.backend.enums.UserRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {
    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Email
    private String email;

    @EnumValue(enumClass = UserRoles.class)
    private UserRoles role;
}
