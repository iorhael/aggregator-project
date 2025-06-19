package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Schema(description = "DTO for filtering products based on criteria")
public class ProductFilterDto {

    @Schema(description = "Product name or part of it", example = "iPhone")
    @JsonProperty("product_name")
    private String productName;

    @Schema(description = "Vendor/manufacturer name", example = "Apple")
    @JsonProperty("vendor_name")
    private String vendorName;

    @Schema(description = "Category name of the product", example = "Smartphones")
    @JsonProperty("category_name")
    private String categoryName;

    @Schema(description = "Verification status (true = only verified)")
    @JsonProperty("verification_status")
    private Boolean verificationStatus;

    @Schema(description = "Minimum price", example = "300")
    @JsonProperty("min_price")
    private BigDecimal minPrice;

    @Schema(description = "Minimum number of product cards/offers", example = "2")
    @JsonProperty("min_offers_count")
    private Integer minOffersCount;

    @Schema(description = "Minimum average rating", example = "4.2")
    @JsonProperty("average_rating")
    private BigDecimal averageRating;

    @Schema(description = "Map of technical characteristics (key = characteristic, value = required value)",
            example = "{\"RAM\":\"8GB\", \"CPU\":\"Intel i5\"}"
    )
    @JsonProperty("characteristics")
    private Map<String, String> characteristics;
}
