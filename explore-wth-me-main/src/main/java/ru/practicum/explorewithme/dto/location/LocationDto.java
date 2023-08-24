package ru.practicum.explorewithme.dto.location;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.model.enums.LocationStatus;

@Data
@Builder
public class LocationDto {
    private String name;
    private Double lat;
    private Double lon;
    private Double rad;
    private Long id;
    private LocationStatus status;
}
