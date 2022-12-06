package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exceptions.IncorrectParameterException;
import ru.practicum.ewm.stats.clients.EventClient;
import ru.practicum.ewm.stats.hit.Stat;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventClient eventClient;
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) String text,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) boolean paid,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort,
                                        @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size,
                                        HttpServletRequest request) {

        log.info("Event Client add request in getEvents method.");
        eventClient.addHit(request);
        System.out.println(request.getRequestURI());

        Stat[] stats;
        try {
            stats = eventClient.getEventStats(rangeStart, rangeEnd, request.getRequestURI(), false).getBody();
            System.out.println(Arrays.toString(stats));
        } catch (Exception e) {
            throw new IncorrectParameterException("Event Client request error. Views not updated.");
        }

        log.info("Public Event Controller: Get Events with" +
                        " text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}.",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, sort, onlyAvailable, from, size, stats, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long eventId,
                                 HttpServletRequest request) {

        log.info("Event Client add request in getEvent method.");
        eventClient.addHit(request);

        Stat[] stats;
        try {
            stats = eventClient.getEventStats(null, null, request.getRequestURI(), false).getBody();
            System.out.println(Arrays.toString(stats));
        } catch (Exception e) {
            throw new IncorrectParameterException("Event Client request error. Views not updated.");
        }

        return eventService.getEvent(eventId, stats, request);
    }
}