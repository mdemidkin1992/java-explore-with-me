package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.service.category.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoriesController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addNewCategory(
            @RequestBody @Valid NewCategoryDto dto
    ) {
        log.info("POST | Add new Category: {}", dto);
        CategoryDto response = categoryService.addNewCategory(dto);
        log.info("Add new Category successfully: {}", response);
        return response;
    }

    @DeleteMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @PathVariable(name = "catId") long catId
    ) {
        log.info("DELETE | CatId: {} | Delete Category", catId);
        categoryService.deleteCategory(catId);
        log.info("Delete Category successfully");
    }

    @PatchMapping(path = "/{catId}")
    public CategoryDto updateCategory(
            @PathVariable(name = "catId") long catId,
            @RequestBody @NotNull @Valid CategoryDto dto
    ) {
        log.info("PATCH | CatId: {} | Update Category", catId);
        CategoryDto response = categoryService.updateCategory(catId, dto);
        log.info("Update Category successfully: {}", response);
        return response;
    }

}
