package com.senla.aggregator.dto.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.UserTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO for returning detailed favorite product information")
public class FavoriteGetDto {

    @Schema(description = "Unique identifier of the favorite entry")
    @JsonProperty("id")
    private UUID id;

    @Schema(description = "ID of the favorited product")
    @JsonProperty("product_id")
    private UUID productId;

    @Schema(description = "Name of the product", example = "iPhone 15")
    @JsonProperty("product_name")
    private String productName;

    @Schema(
            description = "User's personal notes about the product",
            example = "Waiting for a discount"
    )
    @JsonProperty("user_notes")
    private String notes;

    @Schema(description = "User-defined tag for the favorite")
    @JsonProperty("user_tag")
    private UserTag tag;

    @Schema(description = "Name of the product's vendor", example = "Apple")
    @JsonProperty("vendor_name")
    private String vendorName;

    @Schema(description = "Lowest available price", example = "999.99")
    @JsonProperty("minimal_price")
    private BigDecimal minimalPrice;

    @Schema(description = "Number of offers for this product", example = "8")
    @JsonProperty("offers_count")
    private Integer offersCount;

    @Schema(description = "Average product rating", example = "4.6")
    @JsonProperty("average_rating")
    private BigDecimal averageRating;

    @Schema(description = "Whether this product is verified")
    @JsonProperty("is_verified")
    private Boolean verified;
}
