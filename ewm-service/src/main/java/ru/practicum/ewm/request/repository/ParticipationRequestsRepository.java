package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestsRepository extends JpaRepository<ParticipationRequest, Long> {
    Long countParticipationRequestByEventIdAndStatus(Long eventId, String status);

    boolean existsByRequesterIdAndEventId(Long requestorId, Long eventId);

    List<ParticipationRequest> getAllByRequesterId(Long userId);

    List<ParticipationRequest> getAllByEventId(Long eventId);

    List<ParticipationRequest> getAllByStatusAndEventId(String status, Long eventId);
}
