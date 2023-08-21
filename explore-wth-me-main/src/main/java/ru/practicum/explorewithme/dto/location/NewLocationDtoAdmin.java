package ru.practicum.explorewithme.dto.location;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class NewLocationDtoAdmin {
    @NotBlank
    private String name;
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
    @Min(0)
    private Double rad;
    private Long id;
}
