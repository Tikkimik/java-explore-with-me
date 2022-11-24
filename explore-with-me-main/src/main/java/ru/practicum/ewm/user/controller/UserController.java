package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.CreateUserDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody CreateUserDto user) {
        log.info("Create new user.");
        return userService.createUser(user);
    }

    @GetMapping
    public List<UserDto> findUsersByIds(@RequestParam("ids") List<Long> usersIds) {
        log.info("Get user(s) by user id(s).");
        return userService.findUsersByIds(usersIds);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info("Delete user by user id.");
        userService.deleteUserById(userId);
    }
}
