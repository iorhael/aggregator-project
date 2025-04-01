package com.senla.aggregator.service.product;

import com.senla.aggregator.dto.product.ProductCreateDto;
import com.senla.aggregator.dto.product.ProductDetailedDto;
import com.senla.aggregator.dto.product.ProductFilterDto;
import com.senla.aggregator.dto.product.ProductInfoDto;
import com.senla.aggregator.dto.product.ProductNameDescriptionDto;
import com.senla.aggregator.dto.product.ProductPreviewDto;
import com.senla.aggregator.dto.product.ProductUpdateDto;
import com.senla.aggregator.mapper.ProductMapper;
import com.senla.aggregator.model.Category;
import com.senla.aggregator.model.Product;
import com.senla.aggregator.model.Product_;
import com.senla.aggregator.repository.category.CategoryRepository;
import com.senla.aggregator.repository.product.ProductRepository;
import com.senla.aggregator.repository.vendor.VendorRepository;
import com.senla.aggregator.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    private final VendorRepository vendorRepository;

    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDetailedDto getProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        return productMapper.toProductDetailedDto(product);
    }

    @Override
    @Transactional
    public ProductInfoDto createProduct(ProductCreateDto dto, Boolean isCreatorTrusted) {
        Product product = productMapper.toProduct(dto);

        product.setVendor(vendorRepository.getReferenceById(dto.getVendorId()));
        product.setCategories(dto.getCategoriesIds()
                .stream()
                .map(categoryRepository::getReferenceById)
                .toList());
        product.setVerified(isCreatorTrusted);

        return productMapper.toProductInfoDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public List<ProductNameDescriptionDto> getProductsNameDescription(UUID categoryId, int pageNo, int pageSize) {
        return productRepository.findAllProductsInCategoryTree(categoryId, PageRequest.of(pageNo, pageSize,
                        Sort.by(Product_.NAME)))
                .stream()
                .map(productMapper::toProductNameDescriptionDto)
                .toList();
    }

    @Override
    @Transactional
    public List<ProductPreviewDto> getProductsPreviews(UUID categoryId, int pageNo, int pageSize) {
        return productRepository.findAllProductsInCategoryTree(categoryId, PageRequest.of(pageNo, pageSize,
                        Sort.by(Product_.NAME)))
                .stream()
                .map(productMapper::toProductPreviewDto)
                .toList();
    }

    @Override
    @Transactional
    public List<ProductPreviewDto> filterProducts(ProductFilterDto dto,
                                                  int pageNo,
                                                  int pageSize) {
        List<String> childCategoryNames = new ArrayList<>();
        if (Objects.nonNull(dto.getCategoryName()) && !dto.getCategoryName().isBlank()) {
            childCategoryNames = categoryRepository.findChildren(dto.getCategoryName())
                    .stream()
                    .map(Category::getName)
                    .toList();
        }

        Specification<Product> specification = ProductSpecification.buildSpecification(
                dto.getProductName(),
                dto.getVendorName(),
                dto.getVerificationStatus(),
                dto.getMinPrice(),
                dto.getMinOffersCount(),
                dto.getAverageRating(),
                childCategoryNames,
                dto.getCharacteristics()
        );

        List<Product> products = productRepository.findAll(specification, PageRequest.of(pageNo, pageSize,
                        Sort.by(Product_.NAME)))
                .toList();

        return products.stream()
                .map(productMapper::toProductPreviewDto)
                .toList();
    }

    @Override
    @Transactional
    public int verifyProducts(List<UUID> productIds) {
        return productRepository.verifyProducts(productIds);
    }

    @Override
    @Transactional
    public ProductInfoDto updateProduct(ProductUpdateDto dto, UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        productMapper.updateProduct(product, dto);
        product.setVerified(true);

        return productMapper.toProductInfoDto(product);
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
