package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {

    UserDto add(UserDto newUserDto);

    List<UserDto> get(List<Long> ids, Integer from, Integer size);

    User getUserById(Long userId);

    void delete(Long userId);
}
