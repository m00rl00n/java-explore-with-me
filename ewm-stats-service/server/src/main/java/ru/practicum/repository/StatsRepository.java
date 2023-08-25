package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.StatsHit;
import ru.practicum.model.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<StatsHit, Integer> {
    @Query(" select new ru.practicum.model.Stats(h.app, h.uri, count(h.ip)) from Hit h " +
            "where (h.uri in (?1)) and (h.timestamp between ?2 and ?3) " +
            "group by h.app, h.uri")
    List<StatsResponse> getStats(List<String> uri, LocalDateTime start, LocalDateTime end);

    @Query(" select new ru.practicum.model.Stats(h.app, h.uri, count(distinct h.ip)) from Hit h " +
            "where (h.uri in (?1)) and (h.timestamp between ?2 and ?3) " +
            "group by h.app, h.uri")
    List<StatsResponse> getUniqueStats(List<String> uri, LocalDateTime start, LocalDateTime end);

    @Query(" select new  ru.practicum.model.Stats(h.app, h.uri, count(h.ip)) from Hit h " +
            "where (h.timestamp between ?1 and ?2) " +
            "group by h.app, h.uri")
    List<StatsResponse> getStatsWithUriIsNull(LocalDateTime start, LocalDateTime end);

    @Query(" select new ru.practicum.model.Stats(h.app, h.uri, count(distinct h.ip)) from Hit h " +
            "where (h.timestamp between ?1 and ?2) " +
            "group by h.app, h.uri")
    List<StatsResponse> getUniqueStatsWithUriIsNull(LocalDateTime start, LocalDateTime end);
}

