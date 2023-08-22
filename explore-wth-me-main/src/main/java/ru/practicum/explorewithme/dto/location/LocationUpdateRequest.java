package ru.practicum.explorewithme.dto.location;

import lombok.Data;

@Data
public class LocationUpdateRequest {
    private String name;
    private Double rad;
    private String status;
}
