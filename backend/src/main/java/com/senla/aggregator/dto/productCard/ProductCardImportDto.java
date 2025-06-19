package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.OnCreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductCardImportDto {

    @NotBlank
    @JsonProperty("product_name")
    private String productName;

    @NotBlank(groups = OnCreateGroup.class)
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

    @NotNull(groups = OnCreateGroup.class)
    @Positive
    @JsonProperty("price")
    private BigDecimal price;
}
