package cz.promtply.backend.dto.user;

import cz.promtply.backend.dto.UpdatedByResponseDto;
import cz.promtply.backend.enums.UserRolesEnum;
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
