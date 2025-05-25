package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO used to update existing product")
public class ProductUpdateDto {

    @Schema(description = "Updated product name", example = "Galaxy S23 Ultra")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Updated description", example = "Flagship Samsung phone")
    @JsonProperty("description")
    private String description;

    @Schema(
            description = "Updated technical characteristics in JSON",
            example = "{\"Camera\":\"200MP\", \"Storage\":\"512GB\"}"
    )
    @JsonProperty("technical_characteristics")
    private JsonNode characteristics;
}
