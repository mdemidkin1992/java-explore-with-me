package ru.practicum.explorewithme.dto.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.user.mapper.UserMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

    public Event mapFromNewEventDto(NewEventDto dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .category(Category.builder().id(dto.getCategory()).build())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .location(dto.getLocation())
                .paid(dto.getPaid() != null && dto.getPaid())
                .participantLimit(dto.getParticipantLimit() == null ? 0 : dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration() == null || dto.getRequestModeration())
                .title(dto.getTitle())
                .build();
    }

    public EventFullDto mapEventFullDtoFromEntity(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapDtoFromEntity(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(String.valueOf(event.getState()))
                .title(event.getTitle())
                .build();
    }

    public EventShortDto mapToEventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapDtoFromEntity(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .id(event.getId())
                .build();
    }

    public List<EventShortDto> mapToEventShortDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    public List<EventFullDto> mapToEventFullDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::mapEventFullDtoFromEntity)
                .collect(Collectors.toList());
    }

}
