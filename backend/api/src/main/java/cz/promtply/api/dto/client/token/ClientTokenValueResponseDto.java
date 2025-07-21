package cz.promtply.api.dto.client.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientTokenValueResponseDto {
    private String token;
}
