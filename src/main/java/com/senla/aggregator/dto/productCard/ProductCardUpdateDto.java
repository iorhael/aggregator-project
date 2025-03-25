package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.ProductCardProperties;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductCardUpdateDto {

    @Positive
    @JsonProperty("new_price")
    private BigDecimal price;

    @JsonProperty("additional_properties")
    private ProductCardProperties additionalProperties;
}
