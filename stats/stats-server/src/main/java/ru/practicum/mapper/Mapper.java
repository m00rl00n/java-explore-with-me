package ru.practicum.mapper;

import ru.practicum.EndpointHitDto;
import ru.practicum.StatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setIp(endpointHit.getIp());
        endpointHitDto.setApp(endpointHit.getApp());
        endpointHitDto.setUri(endpointHit.getUri());
        endpointHitDto.setTimestamp(endpointHit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return endpointHitDto;
    }

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setApp(endpointHitDto.getApp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setTimestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return endpointHit;
    }

    public static StatsDto toStatsDto(Stats stats) {
        StatsDto statsDto = new StatsDto();
        statsDto.setApp(stats.getApp());
        statsDto.setHits(stats.getHits());
        statsDto.setUri(stats.getUri());
        return statsDto;
    }
}
