package ru.practicum.ewm.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.compilation.model.EventCompilation;

public interface EventCompilationRepository extends JpaRepository<EventCompilation, Long> {

    EventCompilation getByCompilationIdAndEventId(Long compId, Long eventId);

    boolean existsByCompilationIdAndEventId(Long compId, Long eventId);

}
