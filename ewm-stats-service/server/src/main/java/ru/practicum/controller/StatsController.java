package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsHitDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.service.StatsServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsServiceImpl service;

    @PostMapping("/hit")
    public StatsHitDto save(@RequestBody StatsHitDto statsHitDto) {
        return service.save(statsHitDto);
    }

    @GetMapping("/stats")
    public List<StatsResponseDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}