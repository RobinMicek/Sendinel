package cz.promtply.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private Instant created_on;
    private Instant updated_on;
    private Instant deleted_on;
}
