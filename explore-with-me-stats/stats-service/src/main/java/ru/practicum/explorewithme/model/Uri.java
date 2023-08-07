package ru.practicum.explorewithme.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "uris")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Uri {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
