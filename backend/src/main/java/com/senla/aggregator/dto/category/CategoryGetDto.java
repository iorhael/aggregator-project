package com.senla.aggregator.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Category data used for responses")
public class CategoryGetDto {

    @Schema(description = "Unique identifier of the category")
    @JsonProperty("category_id")
    private UUID id;

    @Schema(description = "Name of the category", example = "Smartphones")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Description of the category", example = "Latest models of smartphones")
    @JsonProperty("description")
    private String description;
}
