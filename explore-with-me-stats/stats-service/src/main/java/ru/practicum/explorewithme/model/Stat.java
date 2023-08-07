package ru.practicum.explorewithme.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String app;
    @ManyToOne(fetch = FetchType.LAZY)
    private Uri uri;
    private String ip;
    private LocalDateTime timestamp;
}