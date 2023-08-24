package ru.practicum.explorewithme.dto.location;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class NewCoordinatesDto {
    @NotNull
    @Min(-90) @Max(90)
    private Double lat;
    @NotNull
    @Min(-180) @Max(180)
    private Double lon;
}
