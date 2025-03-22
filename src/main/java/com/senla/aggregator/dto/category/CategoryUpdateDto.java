package com.senla.aggregator.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
