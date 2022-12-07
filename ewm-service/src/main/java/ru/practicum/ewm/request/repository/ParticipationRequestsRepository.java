package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestsRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> getAllByEventId(Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByEventAndStatus(Event event, String status);

    boolean existsByRequesterIdAndEventId(Long requestorId, Long eventId);

    Long countParticipationRequestByEventAndStatus(Event event, String status);

    List<Long> countParticipationRequestByEventInAndStatus(List<Event> event, String status);

}
