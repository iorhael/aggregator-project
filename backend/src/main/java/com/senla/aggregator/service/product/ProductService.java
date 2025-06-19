package com.senla.aggregator.service.product;

import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.product.ProductCreateDto;
import com.senla.aggregator.dto.product.ProductDetailedDto;
import com.senla.aggregator.dto.product.ProductFilterDto;
import com.senla.aggregator.dto.product.ProductInfoDto;
import com.senla.aggregator.dto.product.ProductNameDescriptionProjection;
import com.senla.aggregator.dto.product.ProductPreviewDto;
import com.senla.aggregator.dto.product.ProductPreviewProjection;
import com.senla.aggregator.dto.product.ProductUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductDetailedDto getProduct(UUID id);

    ProductInfoDto createProduct(ProductCreateDto product, MultipartFile image, Boolean isCreatorTrusted) throws IOException;

    PaginationStatsDto<ProductPreviewDto> getProducts(int pageNo, int pageSize);

    PaginationStatsDto<ProductNameDescriptionProjection> getProductsNameDescription(UUID categoryId, int pageNo, int pageSize);

    PaginationStatsDto<ProductPreviewProjection> getProductsPreviews(UUID categoryId, int pageNo, int pageSize);

    PaginationStatsDto<ProductPreviewDto> filterProducts(ProductFilterDto dto, int pageNo, int pageSize);

    int verifyProducts(List<UUID> productIds);

    ProductInfoDto updateProduct(ProductUpdateDto product, MultipartFile newImage, UUID id) throws IOException;

    void deleteProduct(UUID id);
}
