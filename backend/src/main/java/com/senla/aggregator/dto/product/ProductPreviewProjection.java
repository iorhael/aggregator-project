package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductPreviewProjection {

    @JsonProperty("id")
    UUID getId();

    @JsonProperty("name")
    String getName();

    @JsonProperty("image_link")
    String getImageLink();

    @JsonProperty("vendor_name")
    String getVendorName();

    @JsonProperty("minimal_price")
    BigDecimal getMinimalPrice();

    @JsonProperty("offers_count")
    Integer getOffersCount();

    @JsonProperty("average_rating")
    BigDecimal getAverageRating();

    @JsonProperty("verified")
    Boolean getVerified();
}
