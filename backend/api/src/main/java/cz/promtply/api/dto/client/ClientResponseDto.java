package cz.promtply.api.dto.client;

import cz.promtply.api.dto.UpdatedByResponseDto;
import cz.promtply.api.dto.sender.SenderBasicsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponseDto {
    private UUID id;
    private String name;
    private String description;
    private SenderBasicsResponseDto sender;
    private UpdatedByResponseDto createdBy;
    private UpdatedByResponseDto updatedBy;
    private Instant createdOn;
    private Instant updatedOn;
    private Instant deletedOn;
}
