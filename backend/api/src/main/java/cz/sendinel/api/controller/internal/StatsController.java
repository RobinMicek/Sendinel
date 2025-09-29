package cz.sendinel.api.controller.internal;

import cz.sendinel.api.controller.InternalControllerBase;
import cz.sendinel.api.dto.stats.StatsRequestDto;
import cz.sendinel.api.dto.stats.StatsResponseDto;
import cz.sendinel.api.service.StatsService;
import cz.sendinel.api.util.MapperUtil;
import cz.sendinel.shared.config.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.INTERNAL_API_ROUTE_PREFIX + "/stats")
@RequiredArgsConstructor
public class StatsController extends InternalControllerBase {

    private final StatsService statsService;

    @PostMapping
    @PreAuthorize("hasAuthority('STATS_READ')")
    public ResponseEntity<StatsResponseDto> getStats(@Valid @RequestBody StatsRequestDto statsRequestDto) {
        return ResponseEntity.ok(MapperUtil.toDto(statsService.getStats(statsRequestDto), StatsResponseDto.class));
    }
}
