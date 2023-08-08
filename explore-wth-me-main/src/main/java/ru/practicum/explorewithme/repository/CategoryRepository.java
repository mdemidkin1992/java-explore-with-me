package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsCategoriesByName(String name);
}
