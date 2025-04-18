package com.senla.aggregator.dto.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.UserTag;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class FavoriteGetDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("user_notes")
    private String notes;

    @JsonProperty("user_tag")
    private UserTag tag;

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
