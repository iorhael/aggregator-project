package com.senla.aggregator.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO used to return comment data to the client")
public class CommentGetDto {

    @Schema(description = "Unique identifier of the comment")
    @JsonProperty("id")
    private UUID id;

    @Schema(
            description = "Display name of the comment author",
            example = "John Doe"
    )
    @JsonProperty("author_name")
    private String authorName;

    @Schema(
            description = "Text content of the comment",
            example = "This is a great product!"
    )
    @JsonProperty("content")
    private String content;

    @Schema(
            description = "Rating for the product (1 to 5)",
            example = "4"
    )
    @JsonProperty("product_rating")
    private Short productRating;
}
