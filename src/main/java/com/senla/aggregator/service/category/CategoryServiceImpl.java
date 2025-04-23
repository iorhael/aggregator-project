package com.senla.aggregator.service.category;

import com.senla.aggregator.dto.category.CategoryCreateDto;
import com.senla.aggregator.dto.category.CategoryGetDto;
import com.senla.aggregator.dto.category.CategoryUpdateDto;
import com.senla.aggregator.dto.category.CategoryWithChildrenDto;
import com.senla.aggregator.mapper.CategoryMapper;
import com.senla.aggregator.model.Category;
import com.senla.aggregator.model.Category_;
import com.senla.aggregator.repository.category.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public CategoryGetDto getCategory(UUID id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toCategoryGetDto)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));
    }

    @Override
    public List<CategoryGetDto> getAllTopLevelCategories(int pageNo, int pageSize) {
        return categoryRepository.findTopLevel(PageRequest.of(pageNo, pageSize,
                        Sort.by(Category_.NAME)))
                .stream()
                .map(categoryMapper::toCategoryGetDto)
                .toList();
    }

    @Override
    public CategoryWithChildrenDto getAllSubcategories(UUID parentId) {
        Category category = categoryRepository.getCategoryTree(parentId);

        return categoryMapper.toCategoryWithChildrenDto(category);
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

//    public Category buildCategoryTreeWithRoot(List<Category> flatList) {
//        Map<UUID, Category> map = new HashMap<>();
//
//        // Гарантируем, что у каждой категории инициализирован children
//        for (Category category : flatList) {
//            category.setChildren(new ArrayList<>());
//            map.put(category.getId(), category);
//        }
//
//        Category root = null;
//
//        for (Category category : flatList) {
//            if (category.getParent() != null) {
//                UUID parentId = category.getParent().getId();
//                Category parent = map.get(parentId);
//                if (parent != null) {
//                    parent.getChildren().add(category);
//                }
//            } else {
//                // если родитель = null — это и есть корень
//                root = category;
//            }
//        }
//
//        return root;
//    }


    public Category buildTree(List<Category> flatList, String rootName) {
        Map<UUID, Category> map = new HashMap<>();
        Category root = null;

        for (Category category : flatList) {
            map.put(category.getId(), category);
            if (rootName.equals(category.getName())) {
                root = category;
            }
        }

        if (Objects.isNull(root)) {
            throw new IllegalStateException("Root category not found: " + rootName);
        }

        for (Category category : flatList) {
            // Не трогаем root — на нём getParent() не вызываем
            if (!Objects.equals(category.getId(), root.getId())) {
                Category parentRef = category.getParent();
                if (Objects.nonNull(parentRef)) {
                    Category parent = map.get(parentRef.getId());
                    if (Objects.nonNull(parent)) {
                        List<Category> children = parent.getChildren();
                        if (Objects.isNull(children)) {
                            children = new ArrayList<>();
                            parent.setChildren(children);
                        }
                        children.add(category);
                    }
                }
            }
        }

        return root;
    }
}
