package ru.practicum.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationDtoMapper;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dto.EventDtoMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationServiceImpl implements CompilationService {

    final CompilationRepository compilationRepository;
    final CompilationDtoMapper compilationDtoMapper;
    final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto add(NewCompilationDto compilationDto) {
        log.info("Добавление подборки.....");
        List<Event> events = getEventsFromDto(compilationDto);
        Compilation compilation = createOrUpdateCompilation(compilationDto, events);
        CompilationDto result = compileDtoWithEvents(compilation);
        log.info("Подборка успешно добавлена.");
        return result;
    }

    @Transactional
    @Override
    public CompilationDto update(Long id, UpdateCompilationRequest compilationDto) {
        log.info("Начало обновления подборки, id={}", id);

        Compilation compilation = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Подборка не существует " + id));

        List<Event> events = getEventsFromDto(compilationDto);
        compilation.setEvents(events);

        if (compilationDto.getTitle() != null) {
            compilation.setTitle(compilationDto.getTitle());
            log.info("Название подборки обновлено: '{}'", compilationDto.getTitle());
        }

        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
            log.info("Статус 'pinned' обновлен: '{}'", compilationDto.getPinned());
        }

        compilation = createOrUpdateCompilation(compilation);

        CompilationDto result = compileDtoWithEvents(compilation);

        log.info("Подборка успешно обновлена, id={}", id);
        return result;
    }

    @Override
    public List<CompilationDto> getAll(String pinned, Integer from, Integer size) {
        log.info("Начало получения подборок событий");

        List<Compilation> compilations = getCompilationsByPinned(pinned, from, size);
        List<CompilationDto> compilationDtos = compileDtosWithEvents(compilations);

        log.info("Получено {} подборок событий", compilations.size());
        return compilationDtos;
    }

    @Override
    public CompilationDto getCompilationById(Long id) {
        log.info("Начало получения информации о подборке, id={}", id);

        Compilation compilation = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Подборка не найдена " + id)
        );
        CompilationDto result = compileDtoWithEvents(compilation);

        log.info("Получена информация о подборке, id={}", id);
        return result;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("Начало удаления подборки, id={}", id);

        Compilation compilation = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Подборка не существует " + id));

        compilationRepository.deleteById(id);

        log.info("Подборка успешно удалена, id={}", id);
    }

    private List<Event> getEventsFromDto(NewCompilationDto compilationDto) {
        List<Event> events = new ArrayList<>();
        if (compilationDto.getEvents() != null) {
            events = eventRepository.findAllByIdIn(compilationDto.getEvents());
        }
        return events;
    }

    private List<Event> getEventsFromDto(UpdateCompilationRequest compilationDto) {
        List<Event> events = new ArrayList<>();
        if (compilationDto.getEvents() != null) {
            events = eventRepository.findAllByIdIn(compilationDto.getEvents());
        }
        return events;
    }

    private Compilation createOrUpdateCompilation(NewCompilationDto compilationDto, List<Event> events) {
        Compilation compilation = compilationDtoMapper.mapNewCompilationDtoToCompilation(compilationDto, events);
        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }
        return compilationRepository.save(compilation);
    }

    private Compilation createOrUpdateCompilation(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    private List<Compilation> getCompilationsByPinned(String pinned, Integer from, Integer size) {
        List<Compilation> compilations;
        if (pinned.isEmpty()) {
            compilations = compilationRepository.findAll(PageRequest.of(from / size, size)).getContent();
        } else {
            Boolean pin = Boolean.parseBoolean(pinned);
            compilations = compilationRepository.findAllByPinned(pin, PageRequest.of(from / size, size));
        }
        return compilations;
    }

    private List<CompilationDto> compileDtosWithEvents(List<Compilation> compilations) {
        List<CompilationDto> compilationDtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            CompilationDto cdto = compileDtoWithEvents(compilation);
            compilationDtos.add(cdto);
        }
        return compilationDtos;
    }

    private CompilationDto compileDtoWithEvents(Compilation compilation) {
        CompilationDto result = compilationDtoMapper.mapCompilationToDto(compilation);
        List<EventShortDto> eventDtos = new ArrayList<>();
        for (Event event : compilation.getEvents()) {
            eventDtos.add(EventDtoMapper.mapEventToShortDto(event));
        }
        result.setEvents(eventDtos);
        return result;
    }
}
