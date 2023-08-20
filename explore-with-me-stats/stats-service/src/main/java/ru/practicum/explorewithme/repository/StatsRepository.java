package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.dto.StatDtoWithHitsProjection;
import ru.practicum.explorewithme.model.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {
    @Query(value =
            "SELECT st.app AS app, st.uri AS uri, count(DISTINCT st.ip) AS hits " +
                    "FROM stats st " +
                    "WHERE (:uris IS NULL OR st.uri IN :uris) " +
                    "AND (CAST(st.timestamp AS DATE) BETWEEN :start AND :end) " +
                    "GROUP BY st.app, st.uri " +
                    "ORDER BY hits DESC",
            nativeQuery = true)
    List<StatDtoWithHitsProjection> getStatsForTimeIntervalUnique(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query(value =
            "SELECT st.app AS app, st.uri AS uri, count(st.ip) AS hits " +
                    "FROM stats st " +
                    "WHERE (:uris IS NULL OR st.uri IN :uris) " +
                    "AND (CAST(st.timestamp AS DATE) BETWEEN :start AND :end) " +
                    "GROUP BY st.app, st.uri " +
                    "ORDER BY hits DESC",
            nativeQuery = true)
    List<StatDtoWithHitsProjection> getStatsForTimeInterval(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );
}