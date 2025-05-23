package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductCardUpdateDto {

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

    @Positive
    @JsonProperty("new_price")
    private BigDecimal price;
}
