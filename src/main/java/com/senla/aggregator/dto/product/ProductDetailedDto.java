package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Detailed product view including characteristics and stats")
public class ProductDetailedDto {

    @Schema(description = "Unique ID of the product")
    @JsonProperty("product_id")
    private UUID id;

    @Schema(description = "Product name", example = "PlayStation 5")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Vendor/manufacturer name", example = "Sony")
    @JsonProperty("vendor_name")
    private String vendorName;

    @Schema(description = "Image link in minio", example = "https://minio.example.com/images")
    @JsonProperty("image_link")
    private String imageLink;

    @Schema(description = "Product description", example = "Next-gen gaming console with 825GB SSD")
    @JsonProperty("description")
    private String description;

    @Schema(
            description = "Technical characteristics in JSON format",
            example = "{\"CPU\":\"AMD Ryzen\",\"RAM\":\"16GB\"}"
    )
    @JsonProperty("technical_characteristics")
    private JsonNode characteristics;

    @Schema(description = "Minimum price from available offers", example = "499.00")
    @JsonProperty("minimal_price")
    private BigDecimal minimalPrice;

    @Schema(description = "Number of offers (product cards)", example = "3")
    @JsonProperty("offers_count")
    private Short offersCount;

    @Schema(description = "Number of comments left by users", example = "150")
    @JsonProperty("comments_count")
    private Short commentsCount;

    @Schema(description = "Average rating given by users", example = "5")
    @JsonProperty("average_rating")
    private Short averageRating;

    @Schema(description = "Whether product is verified by admin")
    @JsonProperty("is_verified")
    private Boolean verified;
}
