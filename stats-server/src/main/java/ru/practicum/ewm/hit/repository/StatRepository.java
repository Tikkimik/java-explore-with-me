package ru.practicum.ewm.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.hit.dto.ReturnStatDto;
import ru.practicum.ewm.hit.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stats, Long> {

//    List<Stats> getStatsByTimestampAfterAndTimestampBefore(LocalDateTime start, LocalDateTime end);
//
//    List<Stats> getStatsByTimestampAfterAndTimestampBeforeAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uri);
//
//    Long countStatsByUri(String uri);
//
//    List<Stats> findDistinctByTimestampBetweenAndUri(LocalDateTime start, LocalDateTime end, List<String> uri, boolean unique);

    @Query("SELECT new ru.practicum.ewm.hit.dto.ReturnStatDto(s.app, s.uri, count (distinct s.ip)) " +
            "FROM Stats as s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "AND s.uri IN ?3 " +
            "GROUP BY s.app, s.uri ")
    List<ReturnStatDto> getAllUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("SELECT new ru.practicum.ewm.hit.dto.ReturnStatDto(s.app, s.uri, count (distinct s.ip)) " +
            "FROM Stats as s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "AND s.uri IN ?3 " +
            "GROUP BY s.app, s.uri ")
    List<ReturnStatDto> getAll(LocalDateTime start, LocalDateTime end, List<String> uri);

}
