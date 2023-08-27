package com.ewm.server.visit.service;

import model.StatsHitDto;
import model.StatsResponseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatsService {

    void create(StatsHitDto dto);

    List<StatsResponseDto> getStats(LocalDateTime start,
                                    LocalDateTime end,
                                    List<String> uris,
                                    Boolean unique);

}
