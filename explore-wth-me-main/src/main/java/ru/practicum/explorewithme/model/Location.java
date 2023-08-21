package ru.practicum.explorewithme.model;

import lombok.*;
import ru.practicum.explorewithme.model.enums.LocationStatus;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double lat;
    private Double lon;
    private Double rad;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LocationStatus status;
}
