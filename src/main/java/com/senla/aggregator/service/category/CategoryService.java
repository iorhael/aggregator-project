package com.senla.aggregator.service.category;

import com.senla.aggregator.dto.category.CategoryCreateDto;
import com.senla.aggregator.dto.category.CategoryGetDto;
import com.senla.aggregator.dto.category.CategoryUpdateDto;
import com.senla.aggregator.dto.category.CategoryWithChildrenDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryGetDto getCategory(UUID id);

    CategoryGetDto createCategory(CategoryCreateDto category);

    List<CategoryGetDto> getAllTopLevelCategories(int pageNo, int pageSize);

    CategoryWithChildrenDto getAllSubcategories(UUID parentId);

    CategoryGetDto updateCategory(CategoryUpdateDto category, UUID id);

    void deleteCategory(UUID id);
}
