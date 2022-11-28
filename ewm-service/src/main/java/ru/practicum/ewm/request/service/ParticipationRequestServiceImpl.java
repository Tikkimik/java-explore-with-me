package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.IncorrectParameterException;
import ru.practicum.ewm.exceptions.UpdateException;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.ParticipationRequestsRepository;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.ewm.request.mapper.RequestMapper.toDto;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestsRepository participationRequestsRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<RequestDto> getUserRequests(Long userId) {

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        return participationRequestsRepository.getAllByRequesterId(userId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto createUserRequest(Long userId, Long eventId) {
        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Wrong event id (eventId).");

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        Event event = eventRepository.getReferenceById(eventId);

        if (Objects.equals(event.getInitiator(), userId))
            throw new UpdateException("you can't make request for your own event");

        if (!event.getState().equals("PUBLISHED"))
            throw new UpdateException("Wrong state. Event not published.");

        if (Objects.equals(event.getParticipantLimit(), participationRequestsRepository.countParticipationRequestByEventIdAndStatus(event.getId(), "CONFIRMED")))
            throw new UpdateException("Event no places.");

        if (participationRequestsRepository.existsByRequesterIdAndEventId(userId, event.getId()))
            throw new UpdateException("Request already exist");

        ParticipationRequest request = new ParticipationRequest(
                LocalDateTime.now(),
                eventId,
                userId,
                RequestStatus.PENDING.toString()
        );

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0)
            request.setStatus(RequestStatus.CONFIRMED.toString());

        return toDto(participationRequestsRepository.save(request));
    }

    @Override
    public RequestDto cancelUserRequest(Long userId, Long requestId) {

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        if (!participationRequestsRepository.existsById(requestId))
            throw new UpdateException("Wrong request id (requestId).");

        ParticipationRequest request = participationRequestsRepository.getReferenceById(requestId);
        request.setStatus(RequestStatus.CANCELED.toString());
        return toDto(participationRequestsRepository.save(request));
    }
}