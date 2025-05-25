package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Lightweight product view with name and description")
public class ProductNameDescriptionDto {

    @Schema(description = "Unique ID of the product")
    @JsonProperty("id")
    private UUID id;

    @Schema(description = "Product name", example = "MacBook Air")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Short description", example = "Thin and light laptop with M2 chip")
    @JsonProperty("description")
    private String description;

    @Schema(description = "Verification status")
    @JsonProperty("is_verified")
    private Boolean verified;
}
