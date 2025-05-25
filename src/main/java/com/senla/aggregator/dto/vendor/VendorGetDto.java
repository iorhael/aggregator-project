package com.senla.aggregator.dto.vendor;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO representing a vendor")
public class VendorGetDto {

    @Schema(description = "Vendor unique identifier")
    @JsonProperty("id")
    private UUID id;

    @Schema(description = "Vendor name", example = "Apple")
    @JsonProperty("name")
    private String name;

    @Schema(
            description = "Vendor description",
            example = "Manufacturer of electronics and software"
    )
    @JsonProperty("description")
    private String description;
}
