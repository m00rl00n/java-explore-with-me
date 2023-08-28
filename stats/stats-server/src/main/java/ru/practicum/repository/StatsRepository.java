package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {

    @Query("SELECT new ru.practicum.model.Stats(" +
            "hit.app, hit.uri, " +
            "COUNT(DISTINCT CASE WHEN :uris IS NULL OR hit.uri IN (:uris) THEN hit.ip END)) " +
            "FROM EndpointHit hit " +
            "WHERE hit.timestamp BETWEEN :start AND :end " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY counter DESC")
    List<Stats> findStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );
}