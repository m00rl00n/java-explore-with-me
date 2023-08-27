package com.ewm.server.visit.service;

import com.ewm.server.visit.mapper.StatsMapper;
import com.ewm.server.visit.model.StatsHit;
import com.ewm.server.visit.repository.StatsRepositoryJPA;
import lombok.extern.slf4j.Slf4j;
import model.StatsHitDto;
import model.StatsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private StatsRepositoryJPA statsRepositoryJPA;

    @Autowired
    public StatsServiceImpl(StatsRepositoryJPA statsRepositoryJPA) {
        this.statsRepositoryJPA = statsRepositoryJPA;
    }

    @Override
    @Transactional
    public void create(StatsHitDto dto) {
        StatsHit hitSaved = statsRepositoryJPA.save(StatsMapper.toStatsHit(dto));
        log.info("StatsHit created with id = {}", hitSaved.getId());
    }

    @Override
    public List<StatsResponseDto> getStats(LocalDateTime start,
                                           LocalDateTime end,
                                           List<String> uris,
                                           Boolean unique) {
        return statsRepositoryJPA.findByDate(start, end, uris, unique)
                .stream()
                .map(StatsMapper::toStatsResponseDto)
                .collect(Collectors.toList());
    }
}
