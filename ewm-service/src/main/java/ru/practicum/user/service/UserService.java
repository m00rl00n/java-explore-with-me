package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {

    UserDto add(UserDto userDto);

    List<UserDto> get(List<Long> idList, Integer from, Integer size);

    User getUserById(Long userId);

    void delete(Long userId);
}
