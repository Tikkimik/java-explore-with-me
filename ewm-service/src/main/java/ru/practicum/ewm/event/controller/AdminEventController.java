package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> adminSearchEvents(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<String> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) String rangeStart,
                                                @RequestParam(required = false) String rangeEnd,
                                                @RequestParam(required = false, defaultValue = "0") int from,
                                                @RequestParam(required = false, defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(from, size);
        log.info("Admin Search Events with users={}, states={}, categories={}, " +
                "rangeStart={}, rangeEnd={}, from={}, size={}.", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.adminSearchEvents(users, states, categories, rangeStart, rangeEnd, pageable);
    }

    @PutMapping("/{eventId}")
    public EventFullDto adminUpdateEvent(@PathVariable Long eventId,
                                         @Valid @RequestBody UpdateEventRequest eventDto) {
        log.info("Admin Update Event with eventId={}.", eventId);
        return eventService.adminUpdateEvent(eventId, eventDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto adminPublishEvent(@PathVariable Long eventId) {
        log.info("Admin Publish Event with eventId={}.", eventId);
        return eventService.adminPublishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto adminRejectEvent(@PathVariable Long eventId) {
        log.info("Admin Reject Event with eventId={}.", eventId);
        return eventService.adminRejectEvent(eventId);
    }
}