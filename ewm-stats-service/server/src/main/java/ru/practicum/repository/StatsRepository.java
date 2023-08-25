package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.StatsHit;
import ru.practicum.model.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<StatsHit, Long> {
    List<StatsResponse> getStats(List<String> uri, LocalDateTime start, LocalDateTime end);

    List<StatsResponse> getUniqueStats(List<String> uri, LocalDateTime start, LocalDateTime end);

    List<StatsResponse> getStatsWithUriIsNull(LocalDateTime start, LocalDateTime end);

    List<StatsResponse> getUniqueStatsWithUriIsNull(LocalDateTime start, LocalDateTime end);
}
