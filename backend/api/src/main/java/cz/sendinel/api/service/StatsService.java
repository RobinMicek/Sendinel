package cz.sendinel.api.service;

import cz.sendinel.api.dto.stats.StatsRequestDto;
import cz.sendinel.api.dto.stats.StatsResponseDto;

public interface StatsService {
    StatsResponseDto getStats(StatsRequestDto statsRequestDto);
}
