package com.senla.aggregator.controller;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.category.CategoryCreateDto;
import com.senla.aggregator.dto.category.CategoryGetDto;
import com.senla.aggregator.dto.category.CategoryUpdateDto;
import com.senla.aggregator.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.ControllerMessages.CATEGORY;
import static com.senla.aggregator.controller.ControllerMessages.DELETION_MESSAGE;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    // handle exceptions
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
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseInfoDto deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, CATEGORY, id))
                .build();
    }
}
