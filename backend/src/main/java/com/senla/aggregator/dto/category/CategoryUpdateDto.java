package com.senla.aggregator.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Payload for updating an existing category")
public class CategoryUpdateDto {

    @Schema(
            description = "Updated name of the category",
            example = "Gaming Laptops"
    )
    @JsonProperty("name")
    private String name;

    @Schema(
            description = "Updated description of the category",
            example = "Laptops optimized for gaming performance"
    )
    @JsonProperty("description")
    private String description;
}
