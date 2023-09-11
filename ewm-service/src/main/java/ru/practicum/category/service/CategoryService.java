package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto add(NewCategoryDto newCategoryDto);

    void delete(Long catId);

    CategoryDto update(Long catId, CategoryDto categoryDto);

    CategoryDto getById(Long catId);

    List<CategoryDto> getAll(Integer from, Integer size);
}
