package cz.promtply.api.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientBasicsResponseDto {
    private UUID id;
    private String name;
    private String description;
}
