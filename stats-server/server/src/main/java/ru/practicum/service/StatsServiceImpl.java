package ru.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    @Override
    public StatsHitDto save(StatsHitDto hitDto) {
        StatsHit hit = repository.save(Mapper.toHit(hitDto));
        return Mapper.toHitDto(hit);
    }

    @Override
    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<StatsResponse> statsList;
        if (uris == null) {
            if (unique) {
                statsList = repository.getUniqueStatsWithUriIsNull(start, end);
            } else {
                statsList = repository.getStatsWithUriIsNull(start, end);
            }
        } else {
            if (unique) {
                statsList = repository.getUniqueStats(uris, start, end);
            } else {
                statsList = repository.getStats(uris, start, end);
            }
        }

        return statsList.stream()
                .sorted(Comparator.comparing(StatsResponse::getHits).reversed())
                .map(Mapper::toStatsDto)
                .collect(Collectors.toList());
    }

}