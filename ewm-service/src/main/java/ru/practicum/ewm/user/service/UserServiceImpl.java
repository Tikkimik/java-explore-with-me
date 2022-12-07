package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.user.mapper.UserMapper.toUser;
import static ru.practicum.ewm.user.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {

        if (userRepository.existsByName(userDto.getName()))
            throw new ConflictException("Name already exist.");

        if ((userDto.getEmail() == null) || (!userDto.getEmail().contains("@")))
            throw new ConflictException("Email address null or not valid.");

        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new ConflictException("Email already exist.");

        return toUserDto(userRepository.save(toUser(userDto)));
    }

    @Override
    public List<UserDto> findUsersByIds(List<Long> ids, PageRequest page) {
        return userRepository.getUsersByIdIn(ids, page).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}