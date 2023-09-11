package ru.practicum.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Float lat;
    Float lon;
}
