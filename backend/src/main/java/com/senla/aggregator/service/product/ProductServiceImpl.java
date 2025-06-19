package com.senla.aggregator.service.product;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.product.ProductCreateDto;
import com.senla.aggregator.dto.product.ProductDetailedDto;
import com.senla.aggregator.dto.product.ProductFilterDto;
import com.senla.aggregator.dto.product.ProductInfoDto;
import com.senla.aggregator.dto.product.ProductNameDescriptionProjection;
import com.senla.aggregator.dto.product.ProductPreviewDto;
import com.senla.aggregator.dto.product.ProductPreviewProjection;
import com.senla.aggregator.dto.product.ProductUpdateDto;
import com.senla.aggregator.mapper.ProductMapper;
import com.senla.aggregator.model.Product;
import com.senla.aggregator.model.Product_;
import com.senla.aggregator.repository.ProductRepository;
import com.senla.aggregator.repository.VendorRepository;
import com.senla.aggregator.repository.category.CategoryRepository;
import com.senla.aggregator.service.minio.MinioService;
import com.senla.aggregator.specification.ProductSpecification;
import com.senla.aggregator.util.PaginationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final String PRODUCT_IMAGE_DIRECTORY = "products";

    private final MinioService minioService;
    private final ProductMapper productMapper;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductDetailedDto getProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        return productMapper.toProductDetailedDto(product);
    }

    @Override
    @Transactional
    public ProductInfoDto createProduct(ProductCreateDto dto,
                                        MultipartFile image,
                                        Boolean isCreatorTrusted) throws IOException {
        Product product = productMapper.toProduct(dto);

        if (Objects.nonNull(image)) {
            try (InputStream stream = image.getInputStream()) {
                String imageLink = minioService.saveImage(
                        stream,
                        PRODUCT_IMAGE_DIRECTORY,
                        ContentType.fromValue(image.getContentType())
                );
                product.setImageLink(imageLink);
            }
        }

        product.setVendor(vendorRepository.getReferenceById(dto.getVendorId()));
        product.setCategories(dto.getCategoriesIds()
                .stream()
                .map(categoryRepository::getReferenceById)
                .toList());
        product.setVerified(isCreatorTrusted);

        return productMapper.toProductInfoDto(productRepository.save(product));
    }

    @Override
    public PaginationStatsDto<ProductPreviewDto> getProducts(int pageNo, int pageSize) {
        Page<ProductPreviewDto> contentPage = productRepository.findWithVendorBy(PageRequest.of(pageNo, pageSize,
                        Sort.by(Product_.NAME)))
                .map(productMapper::toProductPreviewDto);

        return new PaginationStatsDto<>(
                contentPage.toList(),
                contentPage.getTotalElements(),
                contentPage.getTotalPages(),
                pageNo,
                pageSize
        );
    }

    @Override
    @Transactional
    public PaginationStatsDto<ProductNameDescriptionProjection> getProductsNameDescription(UUID categoryId,
                                                                                           int pageNo,
                                                                                           int pageSize) {
        List<ProductNameDescriptionProjection> content = productRepository.findAllNameDescriptionInCategoryTree(
                categoryId,
                pageSize,
                pageSize * pageNo
        );
        long productCount = productRepository.countProductsInCategoryTree(categoryId);

        return PaginationUtil.convertToPaginationStatsDto(content, productCount, pageSize, pageNo);
    }

    @Override
    @Transactional
    public PaginationStatsDto<ProductPreviewProjection> getProductsPreviews(UUID categoryId,
                                                                            int pageNo,
                                                                            int pageSize) {
        List<ProductPreviewProjection> content = productRepository.findAllProductsInCategoryTree(
                categoryId,
                pageSize,
                pageSize * pageNo
        );
        long productCount = productRepository.countProductsInCategoryTree(categoryId);

        return PaginationUtil.convertToPaginationStatsDto(content, productCount, pageSize, pageNo);
    }

    @Override
    @Transactional
    public PaginationStatsDto<ProductPreviewDto> filterProducts(ProductFilterDto dto,
                                                                int pageNo,
                                                                int pageSize) {
        List<String> childCategoryNames = new ArrayList<>();
        if (Objects.nonNull(dto.getCategoryName()) && !dto.getCategoryName().isBlank()) {
            childCategoryNames = categoryRepository.getChildrenNamesBy(dto.getCategoryName());
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

        Page<ProductPreviewDto> content = productRepository.findAll(specification, PageRequest.of(pageNo, pageSize,
                        Sort.by(Product_.NAME)))
                .map(productMapper::toProductPreviewDto);

        return PaginationUtil.convertToPaginationStatsDto(content);
    }

    @Override
    @Transactional
    public int verifyProducts(List<UUID> productIds) {
        return productRepository.verifyProducts(productIds);
    }

    @Override
    @Transactional
    public ProductInfoDto updateProduct(ProductUpdateDto dto, MultipartFile newImage, UUID id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        if (Objects.nonNull(newImage)) {
            try (InputStream stream = newImage.getInputStream()) {
                String newImageLink = minioService.updateImage(
                        product.getImageLink(),
                        PRODUCT_IMAGE_DIRECTORY,
                        stream,
                        ContentType.fromValue(newImage.getContentType())
                );
                product.setImageLink(newImageLink);
            }
        }

        productMapper.updateProduct(product, dto);
        product.setVerified(true);

        return productMapper.toProductInfoDto(product);
    }

    @Override
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        if (Objects.nonNull(product.getImageLink()))
            minioService.deleteImage(product.getImageLink());

        productRepository.delete(product);
    }
}
