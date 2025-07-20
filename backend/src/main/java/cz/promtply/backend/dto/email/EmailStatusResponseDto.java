package cz.promtply.backend.dto.email;

import cz.promtply.backend.enums.EmailStatuses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailStatusResponseDto {
    private UUID id;
    private EmailStatuses status;
    private Instant createdOn;
}
