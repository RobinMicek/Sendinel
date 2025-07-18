package cz.promtply.backend.dto.user;

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
    private String role;
    private Instant createdOn;
    private Instant updatedOn;
    private Instant deletedOn;
}
