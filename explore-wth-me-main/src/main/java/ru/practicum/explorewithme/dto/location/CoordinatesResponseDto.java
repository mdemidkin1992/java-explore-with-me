package ru.practicum.explorewithme.dto.location;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoordinatesResponseDto {
    private long id;
    private double lat;
    private double lon;
}