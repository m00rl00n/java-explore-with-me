package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.StatsHitDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.model.StatsHit;
import ru.practicum.model.StatsResponse;

@UtilityClass
public class Mapper {
    public static StatsHit toHit(StatsHitDto statsHitDto) {
        StatsHit statsHit = new StatsHit();
        statsHit.setId(statsHitDto.getId());
        statsHit.setApp(statsHitDto.getApp());
        statsHit.setUri(statsHitDto.getUri());
        statsHit.setIp(statsHitDto.getIp());
        statsHit.setTimestamp(statsHitDto.getTimestamp());
        return statsHit;
    }

    public static StatsHitDto toHitDto(StatsHit statsHit) {
        StatsHitDto statsHitDto = new StatsHitDto();
        statsHitDto.setId(statsHit.getId());
        statsHitDto.setApp(statsHit.getApp());
        statsHitDto.setUri(statsHit.getUri());
        statsHitDto.setIp(statsHit.getIp());
        statsHitDto.setTimestamp(statsHit.getTimestamp());
        return statsHitDto;
    }

    public static StatsResponseDto toStatsDto(StatsResponse statsResponse) {
        StatsResponseDto statsResponseDto = new StatsResponseDto();
        statsResponseDto.setApp(statsResponse.getApp());
        statsResponseDto.setUri(statsResponse.getUri());
        statsResponseDto.setHits(statsResponse.getHits());
        return statsResponseDto;
    }
}
