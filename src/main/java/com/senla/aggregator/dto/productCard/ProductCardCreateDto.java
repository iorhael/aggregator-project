package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.ProductCardProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductCardCreateDto {

    @NotNull
    @JsonProperty("product_id")
    private UUID productId;

    @Positive
    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("additional_properties")
    private ProductCardProperties additionalProperties;
}
