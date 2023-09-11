package ru.practicum.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {

    final UserService userService;

    @GetMapping("/admin/users")
    public List<UserDto> get(@RequestParam(required = false) List<Long> ids,
                             @RequestParam(required = false, defaultValue = "0") Integer from,
                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.get(ids, from, size);
    }

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@Valid @RequestBody UserDto userDto) {
        return userService.add(userDto);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }
}
