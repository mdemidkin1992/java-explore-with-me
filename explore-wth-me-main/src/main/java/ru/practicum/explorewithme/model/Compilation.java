package ru.practicum.explorewithme.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean pinned;

    @ManyToMany(mappedBy = "compilationList")
    private List<Event> eventList = new ArrayList<>();
}
