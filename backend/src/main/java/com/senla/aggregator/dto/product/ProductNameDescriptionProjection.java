package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Lightweight product view with name and description")
public interface ProductNameDescriptionProjection {

    @Schema(description = "Unique ID of the product")
    @JsonProperty("id")
    UUID getId();

    @Schema(description = "Product name", example = "MacBook Air")
    @JsonProperty("name")
    String getName();

    @Schema(description = "Short description", example = "Thin and light laptop with M2 chip")
    @JsonProperty("description")
    String getDescription();

    @Schema(description = "Verification status")
    @JsonProperty("is_verified")
    Boolean getVerified();
}
