package ru.practicum.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsHitDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.service.StatsServiceImpl;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StatsController {

    final StatsServiceImpl statsServiceImpl;

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsResponseDto> getStats(@RequestParam(value = "start") LocalDateTime start,
                                           @RequestParam(value = "end") LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return statsServiceImpl.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatsHitDto save(@RequestBody @Valid StatsHitDto statsHitDto) {
        return statsServiceImpl.save(statsHitDto);
    }
}
