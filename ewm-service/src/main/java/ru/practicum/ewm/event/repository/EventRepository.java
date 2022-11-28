package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> getAllByInitiator(Long userId, PageRequest pageRequest);

    Page<Event> getEventByStateAndCategoryInAndPaid(String published, List<Long> categories, boolean paid, PageRequest pageRequest);

    @Query(value = "select e from Event e join EventCompilation ec on e.id = ec.eventId " +
            "where ec.compilationId = ?1")
    List<Event> getCompilationsEvents(Long compilationId);

    Page<Event> getEventsByStateInAndCategoryInAndInitiatorIn(List<String> state, List<Long> categories, List<Long> users, Pageable pageable);

    Page<Event> getEventsByCategoryInAndInitiatorIn(List<Long> categories, List<Long> users, Pageable pageable);

}
