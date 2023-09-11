package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoriesByName(String name);

    boolean isCategoryExistsByName(String name);

    List<Category> findAllPaged(Integer from, Integer size);
}