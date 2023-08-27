package com.ewm.server.visit.repository;

import com.ewm.server.visit.model.StatsHit;
import com.ewm.server.visit.model.StatsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepositoryJPA extends JpaRepository<StatsHit, Long> {

    @Query(value = "select new com.ewm.server.visit.model.StatsResponse(s.app, s.uri, " +
            "case when ?4 = true then count(distinct s.ip) else count(s.ip) end) " +
            " from StatsHit as s " +
            "where s.timestamp between ?1 and ?2 " +
            "and (s.uri in (?3) or (?3) is null) " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc")
    List<StatsResponse> findByDate(LocalDateTime start,
                                   LocalDateTime end,
                                   List<String> uris,
                                   Boolean unique);
}
