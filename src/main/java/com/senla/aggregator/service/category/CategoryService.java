package com.senla.aggregator.service.category;

import com.senla.aggregator.dto.category.CategoryCreateDto;
import com.senla.aggregator.dto.category.CategoryGetDto;
import com.senla.aggregator.dto.category.CategoryUpdateDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryGetDto createCategory(CategoryCreateDto category);

    CategoryGetDto getCategoryBy(UUID id);

    List<CategoryGetDto> getAllCategories(int pageNo, int pageSize);

    List<CategoryGetDto> getAllSubcategories(String parentName);

    CategoryGetDto updateCategory(CategoryUpdateDto category, UUID id);

    void deleteCategory(UUID id);
}
