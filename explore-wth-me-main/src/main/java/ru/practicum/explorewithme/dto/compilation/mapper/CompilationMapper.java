package ru.practicum.explorewithme.dto.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.event.mapper.EventMapper;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public CompilationDto mapEntityToDto(Compilation compilation) {
        List<Event> eventList = compilation.getEventList() != null ? compilation.getEventList() : new ArrayList<>();

        return CompilationDto.builder()
                .events(EventMapper.mapToEventShortDto(eventList))
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public List<CompilationDto> mapEntityToDto(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public Compilation mapFromNewCompilationDto(NewCompilationDto dto) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned() != null ? dto.getPinned() : false)
                .build();
    }

}
