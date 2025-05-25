package com.senla.aggregator.dto.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.UserTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for updating an existing favorite")
public class FavoriteUpdateDto {

    @Schema(description = "Updated user notes", example = "Purchased already, remove soon")
    @JsonProperty("user_notes")
    private String notes;

    @Schema(description = "Updated user tag")
    @JsonProperty("user_tag")
    private UserTag tag;
}
