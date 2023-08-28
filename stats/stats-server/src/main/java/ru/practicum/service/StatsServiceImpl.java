package ru.practicum.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsDto;
import ru.practicum.mapper.Mapper;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    final StatsRepository statsRepository;

    @Transactional
    public EndpointHitDto save(EndpointHitDto endpointHitDto) {
        return Mapper.toEndpointHitDto(statsRepository.save(Mapper.toEndpointHit(endpointHitDto)));
    }

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<Stats> statsList;
        if (unique) {
            statsList = statsRepository.findStats(start, end, uris);
        } else {
            statsList = statsRepository.findStats(start, end, null);
        }
        return statsList.stream()
                .map(Mapper::toStatsDto)
                .collect(Collectors.toList());
    }
}