package com.senla.aggregator.service.category;

import com.senla.aggregator.dto.category.CategoryCreateDto;
import com.senla.aggregator.dto.category.CategoryGetDto;
import com.senla.aggregator.dto.category.CategoryUpdateDto;
import com.senla.aggregator.mapper.CategoryMapper;
import com.senla.aggregator.model.Category;
import com.senla.aggregator.repository.category.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryGetDto createCategory(CategoryCreateDto dto) {
        Category category = categoryMapper.toCategory(dto);

        Optional.ofNullable(dto.getParentId())
                .map(categoryRepository::getReferenceById)
                .ifPresent(category::setParent);

        categoryRepository.save(category);

        return categoryMapper.toCategoryGetDto(category);
    }

    @Override
    public CategoryGetDto getCategoryBy(UUID id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toCategoryGetDto)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));
    }

    @Override
    public List<CategoryGetDto> getAllCategories(int pageNo, int pageSize) {
        return categoryRepository.findAll(PageRequest.of(pageNo, pageSize))
                .map(categoryMapper::toCategoryGetDto)
                .toList();
    }

    @Override
    public List<CategoryGetDto> getAllSubcategories(String parentName) {
        return categoryRepository.findChildren(parentName)
                .stream()
                .map(categoryMapper::toCategoryGetDto)
                .toList();
    }

    @Override
    @Transactional
    public CategoryGetDto updateCategory(CategoryUpdateDto dto, UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        categoryMapper.updateCategory(category, dto);

        return categoryMapper.toCategoryGetDto(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }
}
