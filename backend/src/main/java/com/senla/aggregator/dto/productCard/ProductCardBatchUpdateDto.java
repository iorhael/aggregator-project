package com.senla.aggregator.dto.productCard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProductCardBatchUpdateDto {

    private UUID id;

    private UUID productId;

    private String productName;

    private UUID retailerId;

    private String description;

    private Short warranty;

    private Short installmentPeriod;

    private Short maxDeliveryTime;
}
