package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductCardPreviewDto {

    @JsonProperty("product_card_id")
    private UUID id;

    @JsonProperty("retailer_name")
    private String retailerName;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("max_delivery_time")
    private Short maxDeliveryTime;
}
