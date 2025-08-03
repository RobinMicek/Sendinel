package cz.sendinel.api.dto.user;

import cz.sendinel.api.dto.constraint.EnumValue;
import cz.sendinel.shared.enums.UserRolesEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDto {
    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Email
    private String email;

    @EnumValue(enumClass = UserRolesEnum.class)
    private UserRolesEnum role;

    @NotBlank
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;
}
