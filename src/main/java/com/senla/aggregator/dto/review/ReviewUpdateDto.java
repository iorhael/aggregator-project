package com.senla.aggregator.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class ReviewUpdateDto {

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @Range(min = 1, max = 5)
    @JsonProperty("product_rating")
    private Short productRating;
}
