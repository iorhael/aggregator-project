package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "DTO for updating an existing product card")
public class ProductCardUpdateDto {

    @Schema(description = "Updated description", example = "Now with free delivery")
    @JsonProperty("description")
    private String description;

    @Positive
    @Schema(description = "Updated warranty period in months", example = "18")
    @JsonProperty("warranty")
    private Short warranty;

    @Positive
    @Schema(description = "Updated installment period in months", example = "10")
    @JsonProperty("installment_period")
    private Short installmentPeriod;

    @Positive
    @Schema(description = "Updated delivery time in days", example = "2")
    @JsonProperty("max_delivery_time")
    private Short maxDeliveryTime;

    @Positive
    @Schema(description = "Updated product price", example = "799.99")
    @JsonProperty("new_price")
    private BigDecimal price;
}
