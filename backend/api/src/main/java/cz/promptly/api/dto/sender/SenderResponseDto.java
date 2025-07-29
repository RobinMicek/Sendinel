package cz.promptly.api.dto.sender;

import com.fasterxml.jackson.databind.JsonNode;
import cz.promptly.api.dto.UpdatedByResponseDto;
import cz.promptly.shared.enums.SenderTypesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenderResponseDto {
    private UUID id;
    private String name;
    private SenderTypesEnum type;
    private JsonNode configuration;
    private UpdatedByResponseDto createdBy;
    private UpdatedByResponseDto updatedBy;
    private Instant createdOn;
    private Instant updatedOn;
    private Instant deletedOn;
}
