package com.senla.aggregator.dto.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.StoreSchedule;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StoreGetDto {

    @JsonProperty("store_id")
    private UUID id;

    @JsonProperty("retailer_name")
    private String retailerName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("opening_hours")
    private StoreSchedule openingHours;
}
