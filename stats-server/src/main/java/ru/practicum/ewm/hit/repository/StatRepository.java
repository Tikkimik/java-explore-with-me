package ru.practicum.ewm.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.hit.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stats, Long> {

    List<Stats> getHitsByTimestampAfterAndTimestampBefore(LocalDateTime start, LocalDateTime end);

    List<Stats> getHitsByTimestampAfterAndTimestampBeforeAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uri);

    Long countStatsByUri(String uri);

}
