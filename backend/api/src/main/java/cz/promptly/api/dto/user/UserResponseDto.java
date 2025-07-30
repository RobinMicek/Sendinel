package cz.promptly.api.dto.user;

import cz.promptly.shared.enums.UserRolesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private UserRolesEnum role;
    private UserBasicsResponseDto createdBy;
    private UserBasicsResponseDto updatedBy;
    private Instant createdOn;
    private Instant updatedOn;
    private Instant deletedOn;
}
