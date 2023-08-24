package ru.practicum.explorewithme.dto.location;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class NewLocationDtoAdmin {
    @NotBlank
    @Size(min = 5, max = 100)
    private String name;
    @NotNull
    @Min(-90) @Max(90)
    private Double lat;
    @NotNull
    @Min(-180) @Max(180)
    private Double lon;
    @PositiveOrZero
    private Double rad;
}
