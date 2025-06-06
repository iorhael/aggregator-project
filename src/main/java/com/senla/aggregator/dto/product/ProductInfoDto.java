package com.senla.aggregator.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Info DTO returned after creation/update of a product")
public class ProductInfoDto {

    @Schema(description = "Unique ID of the product")
    @JsonProperty("id")
    private UUID id;

    @Schema(description = "Product name", example = "Xiaomi Mi Band 7")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Image link in minio", example = "https://minio.example.com/images")
    @JsonProperty("image_link")
    private String imageLink;

    @Schema(description = "Product description", example = "Fitness tracker with AMOLED screen")
    @JsonProperty("description")
    private String description;

    @Schema(
            description = "JSON-formatted technical characteristics",
            example = "{\"Battery life\":\"14 days\"}"
    )
    @JsonProperty("characteristics")
    private JsonNode characteristics;

    @Schema(description = "Is the product verified by an admin")
    @JsonProperty("is_verified")
    private Boolean verified;
}
