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
import ru.practicum.user.dto.NewUserRequest;
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
    public UserDto add(NewUserRequest newUserDto) {
        log.info("Попытка создания пользователя: {}", newUserDto);

        if (userRepository.countByName(newUserDto.getName()) > 0) {
            log.error("Пользователь с именем '{}' уже существует.", newUserDto.getName());
            throw new ConflictException("Пользователь уже существует");
        }

        User user = UserDtoMapper.toNewUser(newUserDto);
        log.debug("Создан пользователь: {}", user);

        User savedUser = userRepository.save(user);
        log.info("Пользователь успешно создан: {}", savedUser);

        return UserDtoMapper.toDto(savedUser);
    }

    @Override
    public List<UserDto> get(List<Long> idList, Integer from, Integer size) {
        log.info("Попытка получения информации о пользователях (from={}, size={})", from, size);
        List<UserDto> userDtos = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);

        if (idList == null) {
            List<User> users = userRepository.findAllPageable(pageable);
            log.debug("Получено {} пользователей", users.size());
            for (User user : users) {
                userDtos.add(UserDtoMapper.toDto(user));
            }
        } else {
            List<User> users = userRepository.findAllByIdsPageable(idList, pageable);
            log.debug("Получено {} пользователей по списку идентификаторов", users.size());
            for (User user : users) {
                userDtos.add(UserDtoMapper.toDto(user));
            }
        }

        return userDtos;
    }

    @Override
    public User getUserById(Long id) {
        log.info("Попытка получения пользователя по ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("Пользователь с ID {} не найден.", id);
            return new NotFoundException("Пользователь с id " + id + " не найден");
        });
        log.debug("Получен пользователь: {}", user);
        return user;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Попытка удаления пользователя по ID: {}", id);

        if (!userRepository.existsById(id)) {
            log.error("Пользователь с ID {} не найден и не может быть удален.", id);
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }

        userRepository.deleteById(id);
        log.info("Пользователь с ID {} успешно удален.", id);
    }
}