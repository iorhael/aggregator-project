package com.senla.aggregator.dto.retailer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for creating a retailer")
public class RetailerCreateDto {

    @Schema(description = "Retailer name", example = "7element")
    @NotBlank
    @JsonProperty("name")
    private String name;

    @Schema(description = "Retailer description", example = "We sell modern electronics")
    @NotBlank
    @JsonProperty("description")
    private String description;

    @Schema(description = "Retailer email", example = "contact@besttech.com")
    @NotBlank
    @Pattern(regexp = ValidationPatterns.EMAIL, message = ValidationPatterns.EMAIL_MESSAGE)
    @JsonProperty("email")
    private String email;

    @Schema(description = "Retailer website URL", example = "https://besttech.com")
    @NotBlank
    @Pattern(regexp = ValidationPatterns.WEBSITE, message = ValidationPatterns.WEBSITE_MESSAGE)
    @JsonProperty("website")
    private String website;
}
