package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductDetailedDto {

    @JsonProperty("product_id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("vendor_name")
    private String vendorName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("technical_characteristics")
    private JsonNode characteristics;

    @JsonProperty("minimal_price")
    private BigDecimal minimalPrice;

    @JsonProperty("offers_count")
    private Short offersCount;

    @JsonProperty("comments_count")
    private Short commentsCount;

    @JsonProperty("average_rating")
    private Short averageRating;

    @JsonProperty("is_verified")
    private Boolean verified;
}
