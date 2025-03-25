package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.ProductCardProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductCardGetDto {

    @JsonProperty("product_card_id")
    private UUID id;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("retailer_name")
    private String retailerName;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("additional_properties")
    private ProductCardProperties additionalProperties;
}
