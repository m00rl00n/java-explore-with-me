package ru.practicum.category.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;


@UtilityClass
public class CategoryDtoMapper {

    public static Category mapNewDtoToCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(null);
        category.setName(categoryDto.getName());
        return category;
    }

    public static CategoryDto mapCategoryToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
