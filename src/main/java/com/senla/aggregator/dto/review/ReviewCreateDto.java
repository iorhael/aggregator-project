package com.senla.aggregator.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

@Getter
@Setter
public class ReviewCreateDto {

    @NotNull
    @JsonProperty("product_id")
    private UUID productId;

    @NotBlank
    @JsonProperty("title")
    private String title;

    @NotBlank
    @JsonProperty("content")
    private String content;

    @NotNull
    @Range(min = 1, max = 5)
    @JsonProperty("product_rating")
    private Short productRating;
}
