package ru.practicum.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsHitDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.mapper.Mapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    final StatsRepository statsRepository;

    @Transactional
    public StatsHitDto save(StatsHitDto statsHitDto) {
        log.info("Новый запрос......");
        return Mapper.toEndpointHitDto(statsRepository.save(Mapper.toEndpointHit(statsHitDto)));
    }

    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            if (uris != null) {
                log.info("Загрузка статистики.....");
                return statsRepository.findUniqueStats(start, end, uris).stream()
                        .map(Mapper::toStatsDto)
                        .collect(Collectors.toList());
            }
            log.info("Загрузка статистики.....");
            return statsRepository.findUniqueStats(start, end).stream()
                    .map(Mapper::toStatsDto)
                    .collect(Collectors.toList());
        }
        if (uris != null) {
            log.info("Загрузка статистики.....");
            return statsRepository.findStats(start, end, uris).stream()
                    .map(Mapper::toStatsDto)
                    .collect(Collectors.toList());
        }
        log.info("Загрузка статистики.....");
        return statsRepository.findStats(start, end).stream()
                .map(Mapper::toStatsDto)
                .collect(Collectors.toList());
    }
}
