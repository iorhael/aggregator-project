package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductPreviewDto {

    @JsonProperty("product_id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("vendor_name")
    private String vendorName;

    @JsonProperty("minimal_price")
    private BigDecimal minimalPrice;

    @JsonProperty("offers_count")
    private Integer offersCount;

    @JsonProperty("average_rating")
    private BigDecimal averageRating;

    @JsonProperty("is_verified")
    private Boolean verified;
}
