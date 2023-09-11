package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto add(CategoryDto newCategoryRequestDto);

    void delete(Long catId);

    CategoryDto update(Long catId, CategoryDto categoryDto);

    CategoryDto getById(Long catId);

    List<CategoryDto> getAll(Integer from, Integer size);
}
