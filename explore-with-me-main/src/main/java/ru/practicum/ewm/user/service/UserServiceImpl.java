package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.user.dto.CreateUserDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

import static ru.practicum.ewm.user.mapper.UserMapper.toUser;
import static ru.practicum.ewm.user.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        return toUserDto(userRepository.save(toUser(createUserDto)));
    }

    @Override
    public List<UserDto> findUsersByIds(List<Long> userIds) {
        return null;
    }

    @Override
    public void deleteUserById(Long userId) {

    }
}
