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
import ru.practicum.model.StatsResponse;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    final StatsRepository statsRepository;

    @Transactional
    public StatsHitDto save(StatsHitDto statsHitDto) {
        log.info("Сохранение статистики....");
        return Mapper.toHitDto(statsRepository.save(Mapper.toHit(statsHitDto)));
    }

    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<StatsResponseDto> statsResponseDtos = new ArrayList<>();
        log.info("Получение статистики.....");
        if (unique) {
            List<StatsResponse> statsList;
            if (uris != null) {
                statsList = statsRepository.findUniqueStats(start, end, uris);
            } else {
                statsList = statsRepository.findUniqueStats(start, end);
            }

            for (StatsResponse stats : statsList) {
                statsResponseDtos.add(Mapper.toStatsDto(stats));
            }
        } else {
            List<StatsResponse> statsList;
            if (uris != null) {
                statsList = statsRepository.findStats(start, end, uris);
            } else {
                statsList = statsRepository.findStats(start, end);
            }

            for (StatsResponse stats : statsList) {
                statsResponseDtos.add(Mapper.toStatsDto(stats));
            }
        }

        return statsResponseDtos;
    }
}
