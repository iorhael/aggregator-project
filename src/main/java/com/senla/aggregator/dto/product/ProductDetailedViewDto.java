package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.ProductCreationType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductDetailedViewDto {

    @JsonProperty("product_id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("categories")
    private List<String> categories;

    @JsonProperty("creation_type")
    private ProductCreationType creationType;

    @JsonProperty("last_update")
    private Instant updatedAt;
}
