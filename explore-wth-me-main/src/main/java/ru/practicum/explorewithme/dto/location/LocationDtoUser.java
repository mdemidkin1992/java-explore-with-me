package ru.practicum.explorewithme.dto.location;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LocationDtoUser {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
    private Double rad;
}
