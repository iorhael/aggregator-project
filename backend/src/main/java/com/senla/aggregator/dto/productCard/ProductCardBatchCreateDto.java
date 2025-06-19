package com.senla.aggregator.dto.productCard;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductCardBatchCreateDto {

    private UUID productId;

    private UUID retailerId;

    private String description;

    private Short warranty;

    private Short installmentPeriod;

    private Short maxDeliveryTime;
}
