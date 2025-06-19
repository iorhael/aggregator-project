package com.senla.aggregator.dto.retailer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO for viewing in retailer info in profile")
public class RetailerWithAutoUpdateCardDto {

    @JsonProperty("id")
    @Schema(description = "Retailer identifier")
    private UUID id;

    @JsonProperty("logo_link")
    @Schema(description = "Download link for organization logo", example = "https://logo-path.com")
    private String logoLink;

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

    @JsonProperty("download_link")
    @Schema(description = "Products feed download link", example = "https://file-path.com")
    private String downloadLink;

    @JsonProperty("verified_products_only")
    @Schema(description = "Upsert product cards only for verified products")
    private Boolean verifiedProductsOnly;
}
