package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Preview data for a product, used in listings and search")
public class ProductPreviewDto {

    @Schema(description = "Unique ID of the product")
    @JsonProperty("id")
    private UUID id;

    @Schema(description = "Product name", example = "iPhone 15 Pro")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Image link in minio", example = "https://minio.example.com/images")
    @JsonProperty("image_link")
    private String imageLink;

    @Schema(description = "Vendor/manufacturer name", example = "Apple")
    @JsonProperty("vendor_name")
    private String vendorName;

    @Schema(description = "Minimum price from all product cards", example = "1299.99")
    @JsonProperty("minimal_price")
    private BigDecimal minimalPrice;

    @Schema(description = "Number of product card offers", example = "5")
    @JsonProperty("offers_count")
    private Integer offersCount;

    @Schema(description = "Average customer rating", example = "4.5")
    @JsonProperty("average_rating")
    private BigDecimal averageRating;

    @Schema(description = "Verification status by admin")
    @JsonProperty("is_verified")
    private Boolean verified;
}
