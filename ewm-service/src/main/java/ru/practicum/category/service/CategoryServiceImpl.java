package ru.practicum.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryDtoMapper;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {

    final CategoryRepository categoryRepository;
    final EventRepository eventRepository;


    @Transactional
    @Override
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        log.info("Добавление новой категории....");
        String categoryName = newCategoryDto.getName();
        if (categoryRepository.existsByName(categoryName)) {
            throw new ConflictException("Категория уже существует");
        }
        Category category = categoryRepository.save(CategoryDtoMapper.toCategory(newCategoryDto));
        log.info("Категория сохранена ");
        return CategoryDtoMapper.toCategoryDto(category);
    }

    @Transactional
    @Override
    public void delete(Long categoryId) {
        log.info("Удаление категории, id = {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не существует "));

        if (!eventRepository.existsByCategoryId(categoryId)) {
            categoryRepository.deleteById(categoryId);
            log.info("Категория удалена ");
        } else {
            throw new ConflictException("Категорию нельзя не удалить ");
        }
    }

    @Transactional
    @Override
    public CategoryDto update(Long categoryId, CategoryDto categoryDto) {
        log.info("Изменение категории, id = {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        List<Category> categoriesWithName = categoryRepository.findByName(categoryDto.getName());
        Long id = null;
        if (!categoriesWithName.isEmpty()) {
            for (Category c : categoriesWithName) {
                if (Objects.equals(c.getId(), categoryId)) {
                    id = c.getId();
                    break;
                }
            }
            if (id == null) {
                throw new ConflictException("Название существует");
            }
        }
        category.setName(categoryDto.getName());
        return CategoryDtoMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        log.info("Получение категорий...");
        List<Category> categories = categoryRepository.findAll(PageRequest.of(from / size, size)).getContent();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            categoryDtos.add(CategoryDtoMapper.toCategoryDto(category));
        }
        return categoryDtos;
    }

    @Override
    public CategoryDto getById(Long categoryId) {
        log.info("Получение информации о категории, id = {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        return CategoryDtoMapper.toCategoryDto(category);
    }
}

