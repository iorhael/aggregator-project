package com.senla.aggregator.repository.productCard;

import com.senla.aggregator.dto.productCard.ProductCardBatchCreateDto;

import java.util.List;

public interface CustomProductCardRepository {
    void batchUpsert(List<ProductCardBatchCreateDto> cards);

    void batchUpdate(List<ProductCardBatchCreateDto> cards);
}
