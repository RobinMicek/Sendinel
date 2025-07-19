package cz.promtply.backend.dto.client;

import cz.promtply.backend.dto.UpdatedByResponseDto;
import cz.promtply.backend.dto.sender.SenderResponseBasicsDto;
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
    private SenderResponseBasicsDto sender;
    private UpdatedByResponseDto createdBy;
    private UpdatedByResponseDto updatedBy;
    private Instant createdOn;
    private Instant updatedOn;
    private Instant deletedOn;
}
