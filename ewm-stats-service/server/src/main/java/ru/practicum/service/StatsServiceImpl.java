package ru.practicum.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsHitDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.mapper.Mapper;
import ru.practicum.model.StatsHit;
import ru.practicum.model.StatsResponse;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsServiceImpl implements StatsService {

    final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Transactional
    public StatsHitDto save(StatsHitDto hitDto) {
        StatsHit hit = statsRepository.save(Mapper.toHit(hitDto));
        log.info("Новый запрос.....");
        return Mapper.toHitDto(hit);
    }

    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<StatsResponse> statsList;
        if (uris == null) {
            if (unique) {
                statsList = statsRepository.getUniqueStatsWithUriIsNull(start, end);
            } else {
                statsList = statsRepository.getStatsWithUriIsNull(start, end);
            }
        } else {
            if (unique) {
                statsList = statsRepository.getUniqueStats(uris, start, end);
            } else {
                statsList = statsRepository.getStats(uris, start, end);
            }
        }
        log.info("Загрузка статистики.....");

        return statsList.stream()
                .sorted(Comparator.comparing(StatsResponse::getHits).reversed())
                .map(Mapper::toStatsDto)
                .collect(Collectors.toList());
    }
}