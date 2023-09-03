package ru.practicum.mapper;

import ru.practicum.StatsHitDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.model.StatsHit;
import ru.practicum.model.StatsResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {

    public static StatsHitDto toHitDto(StatsHit statsHit) {
        StatsHitDto statsHitDto = new StatsHitDto();
        statsHitDto.setIp(statsHit.getIp());
        statsHitDto.setApp(statsHit.getApp());
        statsHitDto.setUri(statsHit.getUri());
        statsHitDto.setTimestamp(statsHit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return statsHitDto;
    }

    public static StatsHit toHit(StatsHitDto statsHitDto) {
        StatsHit statsHit = new StatsHit();
        statsHit.setIp(statsHitDto.getIp());
        statsHit.setApp(statsHitDto.getApp());
        statsHit.setUri(statsHitDto.getUri());
        statsHit.setTimestamp(LocalDateTime.parse(statsHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return statsHit;
    }

    public static StatsResponseDto toStatsDto(StatsResponse stats) {
        StatsResponseDto statsDto = new StatsResponseDto();
        statsDto.setApp(stats.getApp());
        statsDto.setHits(stats.getHits());
        statsDto.setUri(stats.getUri());
        return statsDto;
    }
}