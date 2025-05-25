package com.senla.aggregator.dto.comment;

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
@Schema(description = "DTO used for creating a new comment")
public class CommentCreateDto {

    @Schema(description = "ID of the product the comment belongs to")
    @NotNull
    @JsonProperty("product_id")
    private UUID productId;

    @Schema(
            description = "Text content of the comment",
            example = "Amazing value for money."
    )
    @NotBlank
    @JsonProperty("content")
    private String content;

    @Schema(
            description = "Rating for the product (1 to 5)",
            example = "5"
    )
    @NotNull
    @Range(min = 1, max = 5)
    @JsonProperty("product_rating")
    private Short productRating;
}
