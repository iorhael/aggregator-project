package com.senla.aggregator.service.productCard;

import com.senla.aggregator.dto.productCard.ProductCardCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardDetailedDto;
import com.senla.aggregator.dto.productCard.ProductCardFilterDto;
import com.senla.aggregator.dto.productCard.ProductCardPreviewDto;
import com.senla.aggregator.dto.productCard.ProductCardUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ProductCardService {

    ProductCardDetailedDto getProductCard(UUID id);

    List<ProductCardDetailedDto> getRetailerProductCards(UUID retailerOwnerId, int pageNo, int pageSize);

    List<ProductCardPreviewDto> filterProductCards(ProductCardFilterDto filterDto, UUID productId, int pageNo, int pageSize);

    ProductCardPreviewDto createProductCard(ProductCardCreateDto productCard, UUID retailerOwnerId);

    ProductCardDetailedDto updateProductCard(ProductCardUpdateDto productCard, UUID productCardId, UUID retailerOwnerId);

    void deleteProductCard(UUID productCardId, UUID retailerOwnerId);
}
