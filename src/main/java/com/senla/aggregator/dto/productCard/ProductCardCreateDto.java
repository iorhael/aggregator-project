package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @JsonProperty("description")
    private String description;

    @Positive
    @JsonProperty("warranty")
    private Short warranty;

    @Positive
    @JsonProperty("installment_period")
    private Short installmentPeriod;

    @Positive
    @JsonProperty("max_delivery_time")
    private Short maxDeliveryTime;

    @NotNull
    @Positive
    @JsonProperty("price")
    private BigDecimal price;
}
