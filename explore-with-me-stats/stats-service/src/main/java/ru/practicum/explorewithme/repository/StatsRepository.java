package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.dto.StatDtoWithHits;
import ru.practicum.explorewithme.model.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {

    @Query("SELECT new ru.practicum.explorewithme.dto.StatDtoWithHits(st.app, st.uri.name, count(st.uri)) " +
            "FROM Stat AS st " +
            "WHERE st.timestamp BETWEEN :start AND :end " +
            "AND st.uri.name IN :uris " +
            "GROUP BY st.uri.name, st.app " +
            "ORDER BY count(st.uri) DESC ")
    List<StatDtoWithHits> getStatsForTimeIntervalAndUris(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT new ru.practicum.explorewithme.dto.StatDtoWithHits(st.app, st.uri.name, count(distinct st.uri)) " +
            "FROM Stat AS st " +
            "WHERE st.timestamp BETWEEN :start AND :end " +
            "AND st.uri.name IN :uris " +
            "GROUP BY st.uri.name, st.app " +
            "ORDER BY count(distinct st.uri) DESC ")
    List<StatDtoWithHits> getStatsForTimeIntervalAndUrisUnique(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT new ru.practicum.explorewithme.dto.StatDtoWithHits(st.app, st.uri.name, count(st.uri)) " +
            "FROM Stat AS st " +
            "WHERE st.timestamp BETWEEN :start AND :end " +
            "GROUP BY st.uri.name, st.app " +
            "ORDER BY count(st.uri) DESC ")
    List<StatDtoWithHits> getStatsForTimeInterval(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explorewithme.dto.StatDtoWithHits(st.app, st.uri.name, count(distinct st.uri)) " +
            "FROM Stat AS st " +
            "WHERE st.timestamp BETWEEN :start AND :end " +
            "GROUP BY st.uri.name, st.app " +
            "ORDER BY count(distinct st.uri) DESC ")
    List<StatDtoWithHits> getStatsForTimeIntervalUnique(LocalDateTime start, LocalDateTime end);
}