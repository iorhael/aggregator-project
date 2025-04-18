package com.senla.aggregator.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CommentGetDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("content")
    private String content;

    @JsonProperty("product_rating")
    private Short productRating;
}
