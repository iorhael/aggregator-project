package com.senla.aggregator.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@Schema(description = "DTO used to update an existing product review")
public class ReviewUpdateDto {

    @Schema(description = "Updated title of the review", example = "Now with Android 14 update")
    @JsonProperty("title")
    private String title;

    @Schema(
            description = "Updated content of the review",
            example = "After one month, the phone is still great"
    )
    @JsonProperty("content")
    private String content;

    @Schema(description = "Updated product rating (1 to 5)", example = "4")
    @Range(min = 1, max = 5)
    @JsonProperty("product_rating")
    private Short productRating;
}
