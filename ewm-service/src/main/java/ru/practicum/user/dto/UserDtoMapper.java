package ru.practicum.user.dto;


import lombok.experimental.UtilityClass;
import ru.practicum.user.model.User;

@UtilityClass
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

    public static User mapNewUserRequestToUser(NewUserRequest newUser) {
        return new User(
                null,
                newUser.getEmail(),
                newUser.getName()
        );
    }

}
