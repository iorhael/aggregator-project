package com.senla.aggregator.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReviewGetDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("product_rating")
    private Short productRating;
}
