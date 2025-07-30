package cz.promptly.api.dto.client;

import cz.promptly.api.dto.user.UserBasicsResponseDto;
import cz.promptly.api.dto.sender.SenderBasicsResponseDto;
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
    private UserBasicsResponseDto createdBy;
    private UserBasicsResponseDto updatedBy;
    private Instant createdOn;
    private Instant updatedOn;
    private Instant deletedOn;
}
