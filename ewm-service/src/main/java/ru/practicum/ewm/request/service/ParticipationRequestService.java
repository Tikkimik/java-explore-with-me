package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.RequestDto;
import java.util.List;

public interface ParticipationRequestService {

    List<RequestDto> getUserRequests(Long userId);

    RequestDto createUserRequest(Long userId, Long eventId);

    RequestDto cancelUserRequest(Long userId, Long requestId);

}