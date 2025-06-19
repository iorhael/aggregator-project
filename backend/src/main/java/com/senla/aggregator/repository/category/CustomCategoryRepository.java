package com.senla.aggregator.repository.category;

import com.senla.aggregator.model.Category;

import java.util.UUID;

public interface CustomCategoryRepository {
    Category getCategoryTree(UUID parentId);
}
