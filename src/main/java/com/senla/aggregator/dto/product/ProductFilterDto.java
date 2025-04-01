package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class ProductFilterDto {

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("vendor_name")
    private String vendorName;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("verification_status")
    private Boolean verificationStatus;

    @JsonProperty("min_price")
    private BigDecimal minPrice;

    @JsonProperty("min_offers_count")
    private Integer minOffersCount;

    @JsonProperty("average_rating")
    private BigDecimal averageRating;

    @JsonProperty("characteristics")
    private Map<String, String> characteristics;
}
