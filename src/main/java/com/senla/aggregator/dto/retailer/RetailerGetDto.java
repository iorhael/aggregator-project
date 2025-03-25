package com.senla.aggregator.dto.retailer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RetailerGetDto {

    @JsonProperty("retailer_id")
    private UUID id;

    @JsonProperty("owner_name")
    private String ownerName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("email")
    private String email;

    @JsonProperty("website")
    private String website;
}
