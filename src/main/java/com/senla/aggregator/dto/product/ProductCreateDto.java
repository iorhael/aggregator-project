package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductCreateDto {

    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("vendor_id")
    private UUID vendorId;

    @NotNull
    @JsonProperty("categories_ids")
    private List<UUID> categoriesIds;

    @NotBlank
    @JsonProperty("description")
    private String description;

    @NotNull
    @JsonProperty("technical_characteristics")
    private JsonNode characteristics;
}
