package ru.practicum.explorewithme.service.category;

import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addNewCategory(NewCategoryDto dto);

    void deleteCategory(long catId);

    CategoryDto updateCategory(long catId, CategoryDto dto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(long catId);
}
