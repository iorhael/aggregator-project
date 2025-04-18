package com.senla.aggregator.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.annotation.processing.Generated;
import java.util.UUID;

@Getter
@Setter
public class CommentCreateDto {

    @NotNull
    @JsonProperty("product_id")
    private UUID productId;

    @NotBlank
    @JsonProperty("content")
    private String content;

    @NotNull
    @Range(min = 1, max = 5)
    @JsonProperty("product_rating")
    private Short productRating;
}
