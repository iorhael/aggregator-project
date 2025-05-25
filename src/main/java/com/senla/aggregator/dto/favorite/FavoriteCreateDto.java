package com.senla.aggregator.dto.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.UserTag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO for creating a new favorite")
public class FavoriteCreateDto {

    @Schema(description = "ID of the product to be added to favorites")
    @NotNull
    @JsonProperty("product_id")
    private UUID productId;

    @Schema(description = "Optional user notes", example = "Compare with Samsung Galaxy S24")
    @JsonProperty("user_notes")
    private String notes;

    @Schema(description = "Optional user tag")
    @JsonProperty("user_tag")
    private UserTag tag;
}
