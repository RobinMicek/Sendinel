package cz.promtply.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedByResponseDto {
    private UUID id;
    private String firstname;
    private String lastname;
}
