package cz.promptly.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicsResponseDto {
    private UUID id;
    private String firstname;
    private String lastname;
}
