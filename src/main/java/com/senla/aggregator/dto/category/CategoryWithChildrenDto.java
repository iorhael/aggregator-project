package com.senla.aggregator.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO representing a category with its child subcategories")
public class CategoryWithChildrenDto {

    @Schema(description = "Unique identifier of the category")
    @JsonProperty("category_id")
    private UUID id;

    @Schema(description = "Name of the category", example = "Electronics")
    @JsonProperty("name")
    private String name;

    @Schema(description = "List of child categories under this category")
    @JsonProperty("child_categories")
    private List<CategoryWithChildrenDto> children = new ArrayList<>();
}
