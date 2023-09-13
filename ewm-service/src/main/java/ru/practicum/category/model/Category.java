package ru.practicum.category.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true, nullable = false)
    String name;
}
