package com.senla.aggregator.dto.vendor;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO used to create or update a vendor")
public class VendorCreateDto {

    @Schema(description = "Vendor name", example = "Apple")
    @NotBlank
    @JsonProperty("name")
    private String name;

    @Schema(
            description = "Vendor description",
            example = "Manufacturer of electronics and software"
    )
    @NotBlank
    @JsonProperty("description")
    private String description;
}
