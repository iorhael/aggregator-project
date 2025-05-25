package com.senla.aggregator.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO used to create a new product review")
public class ReviewCreateDto {

    @Schema(description = "ID of the product being reviewed")
    @NotNull
    @JsonProperty("product_id")
    private UUID productId;

    @Schema(description = "Title of the review", example = "Great value for the price")
    @NotBlank
    @JsonProperty("title")
    private String title;

    @Schema(description = "Full review text", example = "The device performs well and has a nice display.")
    @NotBlank
    @JsonProperty("content")
    private String content;

    @Schema(description = "Product rating (1 to 5)", example = "4")
    @NotNull
    @Range(min = 1, max = 5)
    @JsonProperty("product_rating")
    private Short productRating;
}
