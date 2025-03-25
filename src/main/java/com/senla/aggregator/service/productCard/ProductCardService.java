package com.senla.aggregator.service.productCard;

import com.senla.aggregator.dto.productCard.ProductCardCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardGetDto;
import com.senla.aggregator.dto.productCard.ProductCardUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ProductCardService {

    ProductCardGetDto createProductCard(ProductCardCreateDto productCard, UUID ownerId);

    List<ProductCardGetDto> getMyProductCards(UUID ownerId, int pageNo, int pageSize);

    List<ProductCardGetDto> getAllProductCardsOfRetailer(String retailerName, int pageNo, int pageSize);

    List<ProductCardGetDto> getProductCardsBySpecification(
            String productName,
            String retailerName,
            String discount,
            Boolean installmentAvailable,
            String color,
            String ram,
            String size,
            String strap,
            String warranty,
            int pageNo,
            int pageSize
    );

    ProductCardGetDto updateProductCard(ProductCardUpdateDto productCard, UUID id, UUID ownerId);

    void deleteProductCard(UUID id, UUID ownerId);
}
