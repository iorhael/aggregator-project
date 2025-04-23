package com.senla.aggregator.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CategoryWithChildrenDto {

    @JsonProperty("category_id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("child_categories")
    private List<CategoryWithChildrenDto> children = new ArrayList<>();
}
