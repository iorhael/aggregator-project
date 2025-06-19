package com.senla.aggregator.dto.retailer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for updating retailer details")
public class RetailerUpdateDto {

    @Schema(description = "Retailer name", example = "7element")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Retailer description", example = "We sell modern electronics")
    @JsonProperty("description")
    private String description;

    @Schema(description = "Retailer email", example = "contact@besttech.com")
    @Pattern(regexp = ValidationPatterns.EMAIL, message = ValidationPatterns.EMAIL_MESSAGE)
    @JsonProperty("email")
    private String email;

    @Schema(description = "Retailer website URL", example = "https://besttech.com")
    @Pattern(regexp = ValidationPatterns.WEBSITE, message = ValidationPatterns.WEBSITE_MESSAGE)
    @JsonProperty("website")
    private String website;
}
