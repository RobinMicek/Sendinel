package cz.promtply.api.dto.user;

import cz.promtply.api.dto.constraint.EnumValue;
import cz.promtply.shared.enums.UserRolesEnum;
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

    @EnumValue(enumClass = UserRolesEnum.class)
    private UserRolesEnum role;
}
