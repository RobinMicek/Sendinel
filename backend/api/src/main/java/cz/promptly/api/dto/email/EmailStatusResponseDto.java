package cz.promptly.api.dto.email;

import cz.promptly.shared.enums.EmailStatusesEnum;
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
    private String note;
    private Instant createdOn;
}
