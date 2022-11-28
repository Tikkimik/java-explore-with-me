package ru.practicum.ewm.request.mapper;

import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;

public class RequestMapper {
    public static RequestDto toDto(ParticipationRequest request) {
        return new RequestDto(request.getId(), request.getCreated(), request.getEventId(), request.getRequesterId(), request.getStatus());
    }
}
