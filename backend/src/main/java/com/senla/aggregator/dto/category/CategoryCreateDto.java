package com.senla.aggregator.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Payload for creating a new category")
public class CategoryCreateDto {

    @Schema(description = "Name of the category", example = "Laptops")
    @NotBlank
    @JsonProperty("name")
    private String name;

    @Schema(
            description = "Description of the category",
            example = "High-performance laptops for professionals"
    )
    @NotBlank
    @JsonProperty("description")
    private String description;

    @Schema(description = "Optional parent category ID")
    @JsonProperty("parent_id")
    private UUID parentId;
}
