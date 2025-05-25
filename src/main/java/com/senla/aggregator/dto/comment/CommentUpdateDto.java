package com.senla.aggregator.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@Schema(description = "DTO used for updating an existing comment")
public class CommentUpdateDto {

    @Schema(
            description = "Updated text content of the comment",
            example = "Updated review after more use"
    )
    @NotBlank
    @JsonProperty("content")
    private String content;

    @Schema(
            description = "Updated rating for the product (1 to 5)",
            example = "3"
    )
    @NotNull
    @Range(min = 1, max = 5)
    @JsonProperty("product_rating")
    private Short productRating;
}
