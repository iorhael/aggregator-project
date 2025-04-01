package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductCardDetailedDto {

    @JsonProperty("product_card_id")
    private UUID id;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("retailer_name")
    private String retailerName;

    @JsonProperty("vendor_name")
    private String vendorName;

    @JsonProperty("warranty_period")
    private Short warranty;

    @JsonProperty("installment_period")
    private Short installmentPeriod;

    @JsonProperty("max_delivery_time")
    private Short maxDeliveryTime;

    @JsonProperty("price")
    private BigDecimal price;
}
