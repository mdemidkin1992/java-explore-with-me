package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.dto.StatDtoWithHits;
import ru.practicum.explorewithme.model.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {
    @Query("SELECT new ru.practicum.explorewithme.dto.StatDtoWithHits(st.app, st.uri, count(st.ip)) " +
            "FROM Stat AS st " +
            "WHERE (:uris IS NULL OR st.uri IN :uris) " +
            "AND (:start IS NULL OR st.timestamp >= :start) " +
            "AND (:end IS NULL OR st.timestamp <= :end) " +
            "GROUP BY st.uri, st.app " +
            "ORDER BY count(st.ip) DESC ")
    List<StatDtoWithHits> getStatsForTimeInterval(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query("SELECT new ru.practicum.explorewithme.dto.StatDtoWithHits(st.app, st.uri, count(distinct st.ip)) " +
            "FROM Stat AS st " +
            "WHERE (:uris IS NULL OR st.uri IN :uris) " +
            "AND (:start IS NULL OR st.timestamp >= :start) " +
            "AND (:end IS NULL OR st.timestamp <= :end) " +
            "GROUP BY st.uri, st.app " +
            "ORDER BY count(distinct st.ip) DESC ")
    List<StatDtoWithHits> getStatsForTimeIntervalUnique(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );
}