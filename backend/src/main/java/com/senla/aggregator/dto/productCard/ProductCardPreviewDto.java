package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Preview of a product card")
public class ProductCardPreviewDto {

    @Schema(description = "Unique ID of the product card")
    @JsonProperty("product_card_id")
    private UUID id;

    @Schema(description = "Description provided by the retailer", example = "Brand new with warranty")
    @JsonProperty("description")
    private String description;

    @Schema(description = "Retailer name", example = "7element")
    @JsonProperty("retailer_name")
    private String retailerName;

    @Schema(description = "Retail price", example = "999.99")
    @JsonProperty("price")
    private BigDecimal price;

    @Schema(description = "Warranty period in months", example = "24")
    @JsonProperty("warranty_period")
    private Short warranty;

    @Schema(description = "Installment period in months", example = "12")
    @JsonProperty("installment_period")
    private Short installmentPeriod;

    @Schema(description = "Maximum delivery time in days", example = "2")
    @JsonProperty("max_delivery_time")
    private Short maxDeliveryTime;
}
