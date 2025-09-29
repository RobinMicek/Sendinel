package cz.sendinel.api.dto.stats;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsRequestDto {
    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
