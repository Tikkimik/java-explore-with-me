package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Create new user.");
        return userService.createUser(userDto);
    }

    @GetMapping
    public List<UserDto> findUsersByIds(@RequestParam List<Long> ids,
                                @RequestParam(required = false, defaultValue = "0") int from,
                                @RequestParam(required = false, defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id").ascending());

        log.info("Create new user.");
        return userService.findUsersByIds(ids, pageRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info("User user by userId={}.", userId);
        userService.deleteUserById(userId);
    }
}