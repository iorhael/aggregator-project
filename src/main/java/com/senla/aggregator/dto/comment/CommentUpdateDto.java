package com.senla.aggregator.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class CommentUpdateDto {

    @NotBlank
    @JsonProperty("content")
    private String content;

    @NotNull
    @Range(min = 1, max = 5)
    @JsonProperty("product_rating")
    private Short productRating;
}
