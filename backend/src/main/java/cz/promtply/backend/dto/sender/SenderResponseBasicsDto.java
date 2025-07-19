package cz.promtply.backend.dto.sender;

import cz.promtply.backend.enums.SenderTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenderResponseBasicsDto {
    private UUID id;
    private String name;
    private SenderTypes type;
}
