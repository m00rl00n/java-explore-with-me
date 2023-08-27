package ru.practicum.mapper;

import ru.practicum.StatsHitDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.model.StatsHit;
import ru.practicum.model.StatsResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {

    public static StatsHitDto toEndpointHitDto(StatsHit statsHit) {
        StatsHitDto statsHitDto = new StatsHitDto();
        statsHitDto.setIp(statsHit.getIp());
        statsHitDto.setApp(statsHit.getApp());
        statsHitDto.setUri(statsHit.getUri());
        statsHitDto.setTimestamp(statsHit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return statsHitDto;
    }

    public static StatsHit toEndpointHit(StatsHitDto statsHitDto) {
        StatsHit statsHit = new StatsHit();
        statsHit.setIp(statsHitDto.getIp());
        statsHit.setApp(statsHitDto.getApp());
        statsHit.setUri(statsHitDto.getUri());
        statsHit.setTimestamp(LocalDateTime.parse(statsHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return statsHit;
    }

    public static StatsResponseDto toStatsDto(StatsResponse statsResponse) {
        StatsResponseDto statsResponseDto = new StatsResponseDto();
        statsResponseDto.setApp(statsResponse.getApp());
        statsResponseDto.setHits(statsResponse.getHits());
        statsResponseDto.setUri(statsResponse.getUri());
        return statsResponseDto;
    }
}