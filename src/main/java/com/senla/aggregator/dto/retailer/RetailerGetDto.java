package com.senla.aggregator.dto.retailer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Schema(description = "Retailer data transfer object for response")
public class RetailerGetDto {

    @JsonProperty("retailer_id")
    @Schema(description = "Retailer identifier")
    private UUID id;

    @JsonProperty("name")
    @Schema(description = "Retailer name", example = "7element")
    private String name;

    @JsonProperty("description")
    @Schema(description = "Retailer description", example = "We sell modern electronics")
    private String description;

    @JsonProperty("email")
    @Schema(description = "Retailer email", example = "contact@besttech.com")
    private String email;

    @JsonProperty("website")
    @Schema(description = "Retailer website URL", example = "https://besttech.com")
    private String website;
}
