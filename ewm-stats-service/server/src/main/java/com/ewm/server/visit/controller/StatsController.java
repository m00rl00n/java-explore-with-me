package com.ewm.server.visit.controller;

import com.ewm.server.visit.service.StatsService;
import lombok.extern.slf4j.Slf4j;
import model.StatsHitDto;
import model.StatsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
public class StatsController {
    private StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public HttpStatus createHit(@RequestBody StatsHitDto dto) {
        log.info("PostMapping /hit with uri = {}", dto.getUri());
        statsService.create(dto);
        return HttpStatus.CREATED;
    }

    @GetMapping("/stats")
    public List<StatsResponseDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam(required = false) String[] uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("GetMapping /stats between {} and {}, with uris = {}", start, end, uris);
        return statsService.getStats(start, end, uris != null ? Arrays.asList(uris) : null, unique);
    }

}
