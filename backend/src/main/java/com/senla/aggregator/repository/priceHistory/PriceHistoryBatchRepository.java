package com.senla.aggregator.repository.priceHistory;

import com.senla.aggregator.dto.priceHistory.PriceHistoryBatchCreateDto;

import java.util.List;

public interface PriceHistoryBatchRepository {
    void batchInsert(List<PriceHistoryBatchCreateDto> prices);
}
