package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO for creating a new product card")
public class ProductCardCreateDto {

    @Schema(description = "Associated product ID")
    @NotNull
    @JsonProperty("product_id")
    private UUID productId;

    @Schema(
            description = "Retailer-provided description",
            example = "We have only proven products. 15 years on the market"
    )
    @NotBlank
    @JsonProperty("description")
    private String description;

    @Schema(description = "Warranty period in months", example = "24")
    @Positive
    @JsonProperty("warranty")
    private Short warranty;

    @Schema(description = "Installment period in months", example = "12")
    @Positive
    @JsonProperty("installment_period")
    private Short installmentPeriod;

    @Schema(description = "Maximum delivery time in days", example = "4")
    @Positive
    @JsonProperty("max_delivery_time")
    private Short maxDeliveryTime;

    @Schema(description = "Price of the product", example = "899.99")
    @NotNull
    @Positive
    @JsonProperty("price")
    private BigDecimal price;
}
