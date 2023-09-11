package ru.practicum.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserDto {

    @NotBlank
    @Email
    @Size(min = 6, max = 300)
    String email;
    @NotBlank
    @Size(min = 2, max = 300)
    String name;
}
