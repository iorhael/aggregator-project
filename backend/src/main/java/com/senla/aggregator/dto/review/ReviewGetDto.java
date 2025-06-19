package com.senla.aggregator.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO representing detailed information about a product review")
public class ReviewGetDto {

    @Schema(description = "Unique identifier of the review")
    @JsonProperty("id")
    private UUID id;

    @Schema(description = "Name of the review author", example = "john_doe")
    @JsonProperty("author_name")
    private String authorName;

    @Schema(description = "Name of the reviewed product", example = "Samsung Galaxy S24")
    @JsonProperty("product_name")
    private String productName;

    @Schema(description = "Title of the review", example = "Excellent phone with great camera")
    @JsonProperty("title")
    private String title;

    @Schema(
            description = "Review content text",
            example = "Battery lasts all day and performance is top notch"
    )
    @JsonProperty("content")
    private String content;

    @Schema(description = "Product rating given by the author (1 to 5)", example = "5")
    @JsonProperty("product_rating")
    private Short productRating;

    @Schema(description = "Date of review create")
    @JsonProperty("created_at")
    private Instant createdAt;

    @Schema(description = "Date of last review update")
    @JsonProperty("updated_at")
    private Instant updatedAt;
}
