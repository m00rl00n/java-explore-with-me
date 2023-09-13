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
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {

    final CategoryRepository categoryRepository;
    final EventRepository eventRepository;


    @Transactional
    @Override
    public CategoryDto add(CategoryDto categoryDto) {
        String categoryName = categoryDto.getName();
        log.debug("Имя новой категории: {}", categoryName);

        Category existingCategory = categoryRepository.findCategoriesByName(categoryName);
        if (existingCategory != null) {
            log.warn("Категория с именем '{}' уже существует. Операция отменена.", categoryName);
            throw new ConflictException("Категория уже существует");
        }

        Category category = CategoryDtoMapper.mapNewDtoToCategory(categoryDto);
        log.debug("Создана новая категория: {}", category);

        category = categoryRepository.save(category);
        log.info("Категория успешно сохранена: {}", category);
        return CategoryDtoMapper.mapCategoryToDto(category);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("Удаление категории, id = {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория не существует "));

        if (!eventRepository.existsByCategoryId(id)) {
            categoryRepository.deleteById(id);
            log.info("Категория удалена ");
        } else {
            throw new ConflictException("Категорию нельзя не удалить ");
        }
    }

    @Transactional
    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        log.info("Изменение категории, id = {}", id);
        String newCategoryName = categoryDto.getName();
        Category existingCategory = categoryRepository.findCategoriesByName(newCategoryName);
        if (existingCategory != null && !existingCategory.getId().equals(id)) {
            log.warn("Категория с именем '{}' уже существует. Операция отменена.", newCategoryName);
            throw new ConflictException("Категория с таким именем уже существует");
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        category.setName(newCategoryName);
        category = categoryRepository.save(category);
        log.info("Категория успешно изменена, новое имя: '{}'", newCategoryName);
        return CategoryDtoMapper.mapCategoryToDto(category);
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        log.info("Получение полного списка категорий...");
        List<Category> categories = categoryRepository.findAll(PageRequest.of(from / size, size)).getContent();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            categoryDtos.add(CategoryDtoMapper.mapCategoryToDto(category));
        }
        return categoryDtos;
    }

    @Override
    public CategoryDto getById(Long id) {
        log.info("Получение информации о категории, id = {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Категория с id = {} не найдена. Операция отменена.", id);
                    return new NotFoundException("Категория не найдена");
                });

        return CategoryDtoMapper.mapCategoryToDto(category);
    }
}

