package cz.promptly.api.dto.user;

import cz.promptly.api.dto.UpdatedByResponseDto;
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
    private UpdatedByResponseDto createdBy;
    private UpdatedByResponseDto updatedBy;
    private Instant createdOn;
    private Instant updatedOn;
    private Instant deletedOn;
}
