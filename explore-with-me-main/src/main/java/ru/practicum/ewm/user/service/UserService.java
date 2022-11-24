package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.CreateUserDto;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(CreateUserDto createUserDto);

    List<UserDto> findUsersByIds(List<Long> userIds);

    void deleteUserById(Long userId);

}
