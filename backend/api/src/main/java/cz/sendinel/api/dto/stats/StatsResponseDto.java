package cz.sendinel.api.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponseDto {
    private long totalSent;
    private Map<LocalDate, Long> totalSentDaily;
    private double openRate;
    private double bouncedRate;
    private double failedRate;
    private long uniqueRecipients;
}
