package ru.practicum.mapper;

import ru.practicum.StatsHitDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.model.StatsHit;
import ru.practicum.model.StatsResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {

    public static StatsHitDto toHitDto(StatsHit endpointHit) {
        StatsHitDto statsHitDto = new StatsHitDto();
        statsHitDto.setIp(endpointHit.getIp());
        statsHitDto.setApp(endpointHit.getApp());
        statsHitDto.setUri(endpointHit.getUri());
        statsHitDto.setTimestamp(endpointHit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return statsHitDto;
    }

    public static StatsHit toHit(StatsHitDto statsHitDto) {
        StatsHit endpointHit = new StatsHit();
        endpointHit.setIp(statsHitDto.getIp());
        endpointHit.setApp(statsHitDto.getApp());
        endpointHit.setUri(statsHitDto.getUri());
        endpointHit.setTimestamp(LocalDateTime.parse(statsHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return endpointHit;
    }

    public static StatsResponseDto toStatsDto(StatsResponse stats) {
        StatsResponseDto statsResponseDto = new StatsResponseDto();
        statsResponseDto.setApp(stats.getApp());
        statsResponseDto.setHits(stats.getHits());
        statsResponseDto.setUri(stats.getUri());
        return statsResponseDto;
    }
}

