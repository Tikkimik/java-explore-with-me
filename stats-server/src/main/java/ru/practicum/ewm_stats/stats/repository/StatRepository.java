package ru.practicum.ewm_stats.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_stats.stats.model.Stats;

@Repository
public interface StatRepository  extends JpaRepository<Stats, Long> {
}
