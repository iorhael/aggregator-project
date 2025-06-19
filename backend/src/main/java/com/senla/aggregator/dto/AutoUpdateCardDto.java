package com.senla.aggregator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoUpdateCardDto {

    @NotBlank(groups = OnCreateGroup.class)
    @JsonProperty("download_link")
    private String downloadLink;

    @NotNull(groups = OnCreateGroup.class)
    @JsonProperty("verified_products_only")
    private Boolean verifiedProductsOnly;
}
