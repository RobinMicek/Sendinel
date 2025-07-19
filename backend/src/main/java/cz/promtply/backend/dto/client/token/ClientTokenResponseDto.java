package cz.promtply.backend.dto.client.token;

import cz.promtply.backend.dto.UpdatedByResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientTokenResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Date expiration;
    private boolean isExpired;
    private UpdatedByResponseDto createdBy;
    private UpdatedByResponseDto deletedBy;
    private Instant createdOn;
    private Instant deletedOn;

    // Java does not recognize lombook generated functions inside .stream()
    public void setIsExpired(boolean expired) {
        this.isExpired = expired;
    }
}
