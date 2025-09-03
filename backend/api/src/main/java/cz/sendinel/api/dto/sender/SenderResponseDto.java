package cz.sendinel.api.dto.sender;

import com.fasterxml.jackson.databind.JsonNode;
import cz.sendinel.api.dto.user.UserBasicsResponseDto;
import cz.sendinel.shared.enums.SenderTypesEnum;
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
    private String description;
    private SenderTypesEnum type;
    private JsonNode configuration;
    private UserBasicsResponseDto createdBy;
    private UserBasicsResponseDto updatedBy;
    private Instant createdOn;
    private Instant updatedOn;
    private Instant deletedOn;
}
