package com.senla.aggregator.service.product;

import com.senla.aggregator.dto.product.ProductDetailedViewDto;
import com.senla.aggregator.dto.product.ProductSimpleViewDto;
import com.senla.aggregator.dto.product.ProductUpdateDto;
import com.senla.aggregator.mapper.ProductMapper;
import com.senla.aggregator.model.Category;
import com.senla.aggregator.model.Product;
import com.senla.aggregator.model.ProductCreationType;
import com.senla.aggregator.repository.category.CategoryRepository;
import com.senla.aggregator.repository.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductSimpleViewDto createProduct(ProductSimpleViewDto dto) {
        Product product = productMapper.toProduct(dto);
        List<Category> categories = new ArrayList<>();

        for (String categoryName : dto.getCategories()) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(categoryName);
                        return newCategory;
                    });
            categories.add(category);
        }

        product.setCategories(categories);
        product.setCreationType(ProductCreationType.AUTOMATICALLY_ADDED);

        productRepository.save(product);

        return productMapper.toProductSimpleViewDto(product);
    }

    @Override
    public List<ProductDetailedViewDto> getAllProducts(int pageNo, int pageSize) {
        return productRepository.findWithCategoriesBy(PageRequest.of(pageNo, pageSize,
                Sort.by("name")))
                .stream()
                .map(productMapper::toProductDetailedViewDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductSimpleViewDto updateProduct(ProductUpdateDto dto, UUID id) {
        Product product = productRepository.findWithCategoriesById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        productMapper.updateProduct(product, dto);

        return productMapper.toProductSimpleViewDto(product);
    }

    @Override
    @Transactional
    public ProductSimpleViewDto customizeProduct(ProductUpdateDto dto, UUID id) {
        Product product = productRepository.findWithCategoriesById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));
        List<Category> categories = new ArrayList<>(product.getCategories());

        Product customizedProduct = new Product();
        customizedProduct.setName(dto.getName());

        if (Objects.nonNull(dto.getDescription())) {
            customizedProduct.setDescription(dto.getDescription());
        } else {
            customizedProduct.setDescription(product.getDescription());
        }

        customizedProduct.setParent(product);
        customizedProduct.setCategories(categories);
        customizedProduct.setCreationType(ProductCreationType.CUSTOM);

        productRepository.save(customizedProduct);

        return productMapper.toProductSimpleViewDto(customizedProduct);
    }

    @Override
    public void deleteProduct(UUID id) {
       productRepository.deleteById(id);
    }
}
