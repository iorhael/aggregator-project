package com.senla.aggregator.controller;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.category.CategoryCreateDto;
import com.senla.aggregator.dto.category.CategoryGetDto;
import com.senla.aggregator.dto.category.CategoryUpdateDto;
import com.senla.aggregator.service.category.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.ControllerMessages.CATEGORY;
import static com.senla.aggregator.controller.ControllerMessages.DELETION_MESSAGE;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories source", description = "Manage operations related to categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryGetDto> findAllCategories(@RequestParam(defaultValue = "0") int pageNo,
                                                  @RequestParam(defaultValue = "15") int pageSize) {
        return categoryService.getAllCategories(pageNo, pageSize);
    }

    @GetMapping("/{id}")
    public CategoryGetDto findCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryBy(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryGetDto createCategory(@Valid @RequestBody CategoryCreateDto category) {
        return categoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryGetDto updateCategory(@Valid @RequestBody CategoryUpdateDto category,
                                         @PathVariable UUID id) {
        return categoryService.updateCategory(category, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfoDto deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, CATEGORY, id))
                .build();
    }
}
