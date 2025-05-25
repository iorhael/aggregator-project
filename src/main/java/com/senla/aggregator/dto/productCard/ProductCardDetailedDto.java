package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Detailed view of a product card")
public class ProductCardDetailedDto {

    @Schema(description = "Unique ID of the product card")
    @JsonProperty("product_card_id")
    private UUID id;

    @Schema(description = "Name of the associated product", example = "iPhone 14")
    @JsonProperty("name")
    private String productName;

    @Schema(description = "Description provided by the retailer", example = "Brand new with warranty")
    @JsonProperty("description")
    private String description;

    @Schema(description = "Retailer name", example = "TechStore")
    @JsonProperty("retailer_name")
    private String retailerName;

    @Schema(description = "Vendor name", example = "Apple")
    @JsonProperty("vendor_name")
    private String vendorName;

    @Schema(description = "Warranty period in months", example = "24")
    @JsonProperty("warranty_period")
    private Short warranty;

    @Schema(description = "Installment period in months", example = "12")
    @JsonProperty("installment_period")
    private Short installmentPeriod;

    @Schema(description = "Maximum delivery time in days", example = "3")
    @JsonProperty("max_delivery_time")
    private Short maxDeliveryTime;

    @Schema(description = "Retail price", example = "1099.99")
    @JsonProperty("price")
    private BigDecimal price;
}
