package com.senla.aggregator.dto.priceHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PriceHistoryBatchCreateDto {

    private UUID cardId;

    private BigDecimal price;
}
