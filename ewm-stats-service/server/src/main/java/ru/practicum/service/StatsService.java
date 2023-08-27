package ru.practicum.service;

import model.StatsHitDto;
import model.StatsResponseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    StatsHitDto save(StatsHitDto statsHitDto);

    List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
