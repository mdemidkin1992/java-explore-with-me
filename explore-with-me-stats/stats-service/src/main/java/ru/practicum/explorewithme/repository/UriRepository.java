package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Uri;

public interface UriRepository extends JpaRepository<Uri, Long> {
    Uri findByName(String name);
}
