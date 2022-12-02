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
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.ewm.request.mapper.RequestMapper.toParticipationRequestDto;


@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestsRepository participationRequestsRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<RequestDto> getUserRequests(Long userId) {
        return participationRequestsRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto createUserRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IncorrectParameterException("Event is not found!"));
        User requester = userRepository.findById(userId).orElseThrow(() -> new IncorrectParameterException("User is not found!"));

        if (Objects.equals(event.getInitiator(), requester))
            throw new UpdateException("you can't make request for your own event");

        if (event.getPublishedOn() == null)
            throw new UpdateException("Wrong state. Event not published.");

        if (participationRequestsRepository.getAllByEventId(eventId).size() >= event.getParticipantLimit())
            throw new UpdateException("Event no places.");

        if (participationRequestsRepository.existsByRequesterIdAndEventId(userId, event.getId()))
            throw new UpdateException("Request already exist");

        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(requester);
        request.setEvent(eventRepository.getReferenceById(eventId));
        request.setCreated(LocalDateTime.now());
        request.setInitiator(event.getInitiator());

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0)
            request.setStatus(RequestStatus.CONFIRMED.toString());
         else
            request.setStatus(RequestStatus.PENDING.toString());

        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        return toParticipationRequestDto(participationRequestsRepository.save(request));
    }

    @Override
    public RequestDto cancelUserRequest(Long userId, Long requestId) {

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        if (!participationRequestsRepository.existsById(requestId))
            throw new UpdateException("Wrong request id (requestId).");

        ParticipationRequest request = participationRequestsRepository.getReferenceById(requestId);
        request.setStatus(RequestStatus.CANCELED.toString());
        return toParticipationRequestDto(participationRequestsRepository.save(request));
    }
}