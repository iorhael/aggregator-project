package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO for creating a product")
public class ProductCreateDto {

    @Schema(description = "Product name", example = "Smartphone X")
    @NotBlank
    @JsonProperty("name")
    private String name;

    @Schema(description = "Vendor identifier")
    @NotNull
    @JsonProperty("vendor_id")
    private UUID vendorId;

    @Schema(description = "List of category identifiers the product belongs to")
    @NotNull
    @JsonProperty("categories_ids")
    private List<UUID> categoriesIds;

    @Schema(description = "Detailed product description", example = "High-end smartphone with 128GB storage")
    @NotBlank
    @JsonProperty("description")
    private String description;

    @Schema(
            description = "Technical characteristics in key-value JSON format",
            example = "{\"RAM\":\"8GB\", \"Processor\":\"Snapdragon 8 Gen 2\"}"
    )
    @NotNull
    @JsonProperty("technical_characteristics")
    private JsonNode characteristics;
}
