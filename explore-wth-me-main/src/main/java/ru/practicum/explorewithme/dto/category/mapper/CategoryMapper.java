package ru.practicum.explorewithme.dto.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.model.Category;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CategoryMapper {
    public Category mapEntityFromDto(NewCategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }

    public CategoryDto mapDtoFromEntity(Category entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public List<CategoryDto> mapDtoFromEntity(Iterable<Category> entities) {
        List<CategoryDto> ans = new ArrayList<>();
        for (Category c : entities) {
            ans.add(mapDtoFromEntity(c));
        }
        return ans;
    }
}
