package ru.practicum.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto add(UserDto userDto) {
        log.info("Создание нового пользователя...");
        String username = userDto.getName();
        log.debug("Проверка наличия пользователя с именем: {}", username);

        if (userRepository.countByName(username) > 0) {
            log.error("Пользователь с именем {} уже существует.", username);
            throw new ConflictException("Пользователь с именем " + username + " уже существует.");
        }

        User user = UserDtoMapper.toNewUser(userDto);
        User savedUser = userRepository.save(user);
        log.info("Пользователь успешно создан: ID = {}", savedUser.getId());
        return UserDtoMapper.toDto(savedUser);
    }

    @Override
    public List<UserDto> get(List<Long> idList, Integer from, Integer size) {
        log.info("Получение информации о пользователях...");
        log.debug("Параметры запроса: ids = {}, from = {}, size = {}", idList, from, size);
        List<UserDto> userDtos = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);

        if (idList == null) {
            log.debug("Получение списка всех пользователей с пагинацией...");
            List<User> users = userRepository.findAllUsers(pageable);
            for (User user : users) {
                userDtos.add(UserDtoMapper.toDto(user));
            }
        } else {
            log.debug("Получение списка пользователей по ID с пагинацией...");
            List<User> users = userRepository.findAllUsersByIds(idList, pageable);
            for (User user : users) {
                userDtos.add(UserDtoMapper.toDto(user));
            }
        }

        log.info("Получено {} пользователей.", userDtos.size());
        return userDtos;
    }

    @Override
    public User getUserById(Long id) {
        log.info("Получение пользователя по ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Пользователь с ID " + id + " не найден."));
        log.info("Пользователь успешно найден: ID = {}", user.getId());
        return user;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Удаление пользователя с ID: {}", id);
        userRepository.deleteById(id);
        log.info("Пользователь с ID {} успешно удален.", id);
    }
}
