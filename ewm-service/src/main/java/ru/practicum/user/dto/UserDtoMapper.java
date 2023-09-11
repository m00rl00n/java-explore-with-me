package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.user.model.User;

@Service
@AllArgsConstructor
public class UserDtoMapper {

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static UserShortDto toShortDto(User user) {
        if (user.getId() == null) {
            return new UserShortDto(null, user.getName());
        }
        return new UserShortDto(user.getId(), user.getName());
    }

    public static User toNewUser(UserDto newUser) {
        return new User(
                null,
                newUser.getEmail(),
                newUser.getName()
        );
    }

    public User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getEmail(),
                userDto.getName()
        );
    }
}
