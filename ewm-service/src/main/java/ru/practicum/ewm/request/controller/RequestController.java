package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.service.ParticipationRequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {

    private final ParticipationRequestService requestService;

    @GetMapping
    public List<RequestDto> getUserRequests(@PathVariable Long userId) {
        log.info("Get User Requests with userId={}", userId);
        return requestService.getUserRequests(userId);
    }

    @PostMapping
    public RequestDto createUserRequest(@PathVariable Long userId,
                                        @RequestParam Long eventId) {
        log.info("Create User Requests with userId={}, eventId={}", userId, eventId);
        return requestService.createUserRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelUserRequest(@PathVariable Long userId,
                                        @PathVariable Long requestId) {
        log.info("Cancel User Requests with userId={}, requestId={}", userId, requestId);
        return requestService.cancelUserRequest(userId, requestId);
    }
}