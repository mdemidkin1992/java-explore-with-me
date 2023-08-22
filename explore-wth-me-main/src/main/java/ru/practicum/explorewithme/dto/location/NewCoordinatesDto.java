package ru.practicum.explorewithme.dto.location;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewCoordinatesDto {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}
