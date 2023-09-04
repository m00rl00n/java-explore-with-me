package ru.practicum.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryDtoMapper;
import ru.practicum.category.dto.NewCategoryRequestDto;
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

    @Override
    public CategoryDto add(NewCategoryRequestDto newCategoryRequestDto) {
        log.info("Добавление новой категории....");
        String categoryName = newCategoryRequestDto.getName();
        if (categoryRepository.existsByName(categoryName)) {
            throw new ConflictException("Категория уже существует");
        }
        Category category = categoryRepository.save(CategoryDtoMapper.mapNewDtoToCategory(newCategoryRequestDto));
        log.info("Категория сохранена ");
        return CategoryDtoMapper.mapCategoryToDto(category);
    }

    @Override
    public void delete(Long categoryId) {
        log.info("Удаление категории.....");
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не существует "));

        if (!eventRepository.existsByCategoryId(categoryId)) {
            categoryRepository.deleteById(categoryId);
            log.info("Категория удалена ");
        } else {
            throw new ConflictException("Категорию нельзя не удалить ");
        }
    }

    @Override
    public CategoryDto update(Long categoryId, CategoryDto categoryDto) {
        log.info("Изменение категории....");
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
        return CategoryDtoMapper.mapCategoryToDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        log.info("Получение категорий...");
        List<Category> categories = categoryRepository.findAll(PageRequest.of(from / size, size)).getContent();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            categoryDtos.add(CategoryDtoMapper.mapCategoryToDto(category));
        }
        return categoryDtos;
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        log.info("Получение информации о категории.....");
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        return CategoryDtoMapper.mapCategoryToDto(category);
    }
}

