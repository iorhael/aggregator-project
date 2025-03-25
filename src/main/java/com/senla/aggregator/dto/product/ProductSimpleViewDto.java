package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductSimpleViewDto {
    
    @NotBlank
    @JsonProperty("name")
    private String name;
    
    @NotBlank
    @JsonProperty("description")
    private String description;

    @JsonProperty("categories")
    private List<String> categories;
}
