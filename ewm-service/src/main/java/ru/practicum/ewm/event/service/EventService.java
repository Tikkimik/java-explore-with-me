package ru.practicum.ewm.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.request.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    //PublicEventController
    EventFullDto getEvent(Long eventId, HttpServletRequest request);

    List<EventFullDto> getEvents(String text, List<Long> categories, boolean paid, String rangeStart,
                                 String rangeEnd, String sort, boolean onlyAvailable, int from, int size, HttpServletRequest request);

    //PrivateEventController
    List<EventShortDto> getUserEvents(Long userId, Pageable pageable);

    EventFullDto createUserEvent(Long userId, NewEventDto eventDto);

    EventFullDto updateUserEvent(Long userId, UpdateEventRequest eventDto);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto cancelUserEvent(Long userId, Long eventId);

    List<RequestDto> getUserEventRequests(Long userId, Long eventId);

    RequestDto confirmEventRequest(Long userId, Long eventId, Long reqId);

    RequestDto rejectEventRequest(Long userId, Long eventId, Long reqId);

    //AdminEventController
    List<EventFullDto> adminSearchEvents(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                                         String rangeEnd, Pageable pageable);

    EventFullDto adminUpdateEvent(Long eventId, UpdateEventRequest eventDto);

    EventFullDto adminPublishEvent(Long eventId);

    EventFullDto adminRejectEvent(Long eventId);
}
