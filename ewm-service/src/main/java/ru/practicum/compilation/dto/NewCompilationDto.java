package ru.practicum.compilation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {

    List<Long> events;
    Boolean pinned;
    @NotBlank
    @Size(max = 50)
    String title;
}
