package cz.promtply.backend.dto.email;

import cz.promtply.backend.enums.EmailStatusesEnum;
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
    private EmailStatusesEnum status;
    private Instant createdOn;
}
