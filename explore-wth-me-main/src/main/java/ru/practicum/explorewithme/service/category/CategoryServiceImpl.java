package ru.practicum.explorewithme.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.dto.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.repository.CategoryRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.util.exception.ClientErrorException;
import ru.practicum.explorewithme.util.exception.EntityNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto addNewCategory(NewCategoryDto dto) {
        Category request = CategoryMapper.mapEntityFromDto(dto);
        checkIfCategoryNameExists(dto.getName());
        Category response = categoryRepository.save(request);
        return CategoryMapper.mapDtoFromEntity(response);
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new EntityNotFoundException("Category with id " + catId + " not found.");
        } else {
            if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
                throw new ClientErrorException("For the requested operation the conditions are not met.");
            } else {
                categoryRepository.deleteById(catId);
            }
        }
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(long catId, CategoryDto updateCategoryRequest) {
        Category existingCategory = getCategoryOrThrowException(catId);
        if (!isUpdatedCategoryTheSame(existingCategory, updateCategoryRequest)) {
            checkIfCategoryNameExists(updateCategoryRequest.getName());
        }
        existingCategory.setName(updateCategoryRequest.getName());
        categoryRepository.save(existingCategory);
        return CategoryMapper.mapDtoFromEntity(existingCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        Page<Category> response = categoryRepository.findAll(page);
        return CategoryMapper.mapDtoFromEntity(response);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(long catId) {
        Category category = getCategoryOrThrowException(catId);
        return CategoryMapper.mapDtoFromEntity(category);
    }

    private boolean isUpdatedCategoryTheSame(Category existingCategory, CategoryDto updateCategoryRequest) {
        return existingCategory.getName().equals(updateCategoryRequest.getName());
    }

    private void checkIfCategoryNameExists(String name) {
        if (categoryRepository.existsCategoriesByName(name)) {
            throw new ClientErrorException("Category with name " + name + " already exists");
        }
    }

    private Category getCategoryOrThrowException(long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException("Category with id: " + catId + " was not found")
        );
    }
}
