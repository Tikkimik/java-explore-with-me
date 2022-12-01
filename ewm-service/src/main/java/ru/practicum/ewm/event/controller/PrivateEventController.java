package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.RequestDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Get User Events with from={}, size={}.", from, size);
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping
    public EventFullDto createUserEvent(@PathVariable Long userId,
                                        @Valid @RequestBody NewEventDto eventDto) {
        log.info("Create User Event with userId={}.", userId);
        return eventService.createUserEvent(userId, eventDto);
    }

    @PatchMapping
    public EventFullDto updateUserEvent(@PathVariable Long userId,
                                        @Valid @RequestBody UpdateEventRequest eventDto) {
        log.info("Update User Event with userId={}.", userId);
        return eventService.updateUserEvent(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        log.info("Get User Event with userId={}, eventId={}.", userId, eventId);
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelUserEvent(@PathVariable Long userId,
                                        @PathVariable Long eventId) {
        log.info("Cancel User Event with userId={}, eventId={}.", userId, eventId);
        return eventService.cancelUserEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getUserEventRequests(@PathVariable Long userId,
                                                 @PathVariable Long eventId) {
        log.info("Get User Event Requests with userId={}, eventId={}.", userId, eventId);
        return eventService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmEventRequest(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @PathVariable Long reqId) {
        log.info("Confirm Event Requests with userId={}, eventId={}, reqId={}.", userId, eventId, reqId);
        return eventService.confirmEventRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectEventRequest(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @PathVariable Long reqId) {
        log.info("Reject Event Requests with userId={}, eventId={}, reqId={}.", userId, eventId, reqId);
        return eventService.rejectEventRequest(userId, eventId, reqId);
    }
}