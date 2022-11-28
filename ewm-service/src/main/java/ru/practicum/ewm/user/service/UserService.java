package ru.practicum.ewm.user.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    List<UserDto> findUsersByIds(List<Long> ids, PageRequest page);

    void deleteUserById(Long userId);

}
