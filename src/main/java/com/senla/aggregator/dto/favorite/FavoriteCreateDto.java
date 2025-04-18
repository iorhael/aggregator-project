package com.senla.aggregator.dto.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.UserTag;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FavoriteCreateDto {

    @NotNull
    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("user_notes")
    private String notes;

    @JsonProperty("user_tag")
    private UserTag tag;
}
