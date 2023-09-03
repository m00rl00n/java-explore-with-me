package ru.practicum.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsDto;
import ru.practicum.mapper.Mapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    final StatsRepository statsRepository;

    @Transactional
    public EndpointHitDto save(EndpointHitDto endpointHitDto) {
        log.info("Сохранение статистики....");
        EndpointHit endpointHit = Mapper.toHit(endpointHitDto);
        EndpointHit savedEndpointHit = statsRepository.save(endpointHit);
        return Mapper.toHitDto(savedEndpointHit);
    }

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<Stats> statsList;
        log.info("Получение статистики.....");
        if (unique) {
            if (uris != null) {
                statsList = statsRepository.findUniqueStats(start, end, uris);
            } else {
                statsList = statsRepository.findUniqueStats(start, end);
            }
        } else {
            if (uris != null) {
                statsList = statsRepository.findStats(start, end, uris);
            } else {
                statsList = statsRepository.findStats(start, end);
            }
        }

        List<StatsDto> statsDtoList = new ArrayList<>();
        for (Stats stats : statsList) {
            statsDtoList.add(Mapper.toStatsDto(stats));
        }

        return statsDtoList;
    }
}