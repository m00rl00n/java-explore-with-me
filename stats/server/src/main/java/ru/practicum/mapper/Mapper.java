package ru.practicum.mapper;

import ru.practicum.EndpointHitDto;
import ru.practicum.StatsDto;
import ru.practicum.model.StatsHit;
import ru.practicum.model.StatsResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {

    public static EndpointHitDto toHitDto(StatsHit statsHit) {
        EndpointHitDto statsHitDto = new EndpointHitDto();
        statsHitDto.setIp(statsHit.getIp());
        statsHitDto.setApp(statsHit.getApp());
        statsHitDto.setUri(statsHit.getUri());
        statsHitDto.setTimestamp(statsHit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return statsHitDto;
    }

    public static StatsHit toHit(EndpointHitDto statsHitDto) {
        StatsHit statsHit = new StatsHit();
        statsHit.setIp(statsHitDto.getIp());
        statsHit.setApp(statsHitDto.getApp());
        statsHit.setUri(statsHitDto.getUri());
        statsHit.setTimestamp(LocalDateTime.parse(statsHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return statsHit;
    }

    public static StatsDto toStatsDto(StatsResponse stats) {
        StatsDto statsResponseDto = new StatsDto();
        statsResponseDto.setApp(stats.getApp());
        statsResponseDto.setHits(stats.getHits());
        statsResponseDto.setUri(stats.getUri());
        return statsResponseDto;
    }
}