package ru.practicum.explorewithme.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationDtoWithEvents {
    private String name;
    private Long events;
}
