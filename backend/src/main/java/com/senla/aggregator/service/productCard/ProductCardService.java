package com.senla.aggregator.service.productCard;

import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.productCard.ProductCardCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardDetailedDto;
import com.senla.aggregator.dto.productCard.ProductCardFilterDto;
import com.senla.aggregator.dto.productCard.ProductCardPreviewDto;
import com.senla.aggregator.dto.productCard.ProductCardUpdateDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductCardService {

    ProductCardDetailedDto getProductCard(UUID id);

    PaginationStatsDto<ProductCardDetailedDto> getRetailerProductCards(UUID retailerOwnerId, Pageable pageable);

    long countRetailerProductCards(UUID retailerOwnerId);

    PaginationStatsDto<ProductCardPreviewDto> filterProductCards(ProductCardFilterDto filterDto, UUID productId, int pageNo, int pageSize);

    ProductCardPreviewDto createProductCard(ProductCardCreateDto productCard, UUID retailerOwnerId);

    ProductCardDetailedDto updateProductCard(ProductCardUpdateDto productCard, UUID productCardId, UUID retailerOwnerId);

    void deleteProductCard(UUID productCardId, UUID retailerOwnerId);

    int deleteAllProductCards(UUID retailerOwnerId);
}
