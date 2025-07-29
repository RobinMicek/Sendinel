package cz.promptly.api.dto.sender;

import cz.promptly.shared.enums.SenderTypesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenderBasicsResponseDto {
    private UUID id;
    private String name;
    private String description;
    private SenderTypesEnum type;
}
