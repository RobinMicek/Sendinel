package cz.promtply.backend.dto.client.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientTokenValueResponseDto {
    private String token;
}
