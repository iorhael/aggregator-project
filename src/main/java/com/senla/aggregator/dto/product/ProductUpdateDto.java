package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateDto {

    @NotBlank
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
