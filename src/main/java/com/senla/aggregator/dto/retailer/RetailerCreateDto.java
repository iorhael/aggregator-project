package com.senla.aggregator.dto.retailer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetailerCreateDto {

    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotBlank
    @JsonProperty("description")
    private String description;

    @NotBlank
    @Pattern(regexp = ValidationPatterns.EMAIL,
            message = ValidationPatterns.EMAIL_MESSAGE)
    @JsonProperty("email")
    private String email;

    @NotBlank
    @Pattern(regexp = ValidationPatterns.WEBSITE,
            message = ValidationPatterns.WEBSITE_MESSAGE)
    @JsonProperty("website")
    private String website;
}
