package com.senla.aggregator.dto.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.StoreSchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO representing a store with retailer and schedule info")
public class StoreGetDto {

    @Schema(description = "Unique identifier of the store")
    @JsonProperty("store_id")
    private UUID id;

    @Schema(
            description = "Name of the retailer that owns the store",
            example = "7element"
    )
    @JsonProperty("retailer_name")
    private String retailerName;

    @Schema(
            description = "Store address",
            example = "123 Main Street, Springfield"
    )
    @JsonProperty("address")
    private String address;

    @Schema(description = "Opening hours schedule")
    @JsonProperty("opening_hours")
    private StoreSchedule openingHours;
}
