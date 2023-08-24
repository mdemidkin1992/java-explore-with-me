package ru.practicum.explorewithme.dto.location;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LocationUpdateRequest {
    @Size(min = 5, max = 100)
    private String name;
    private Double rad;
    private AdminStatusUpdate status;

    enum AdminStatusUpdate {
        APPROVED, CANCELED
    }
}
