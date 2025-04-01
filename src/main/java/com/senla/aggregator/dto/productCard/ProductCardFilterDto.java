package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCardFilterDto {

    @JsonProperty("retailer_name")
    private String retailerName;

    @JsonProperty("warranty")
    private Short warranty;

    @JsonProperty("installment_period")
    private Short installmentPeriod;

    @JsonProperty("max_delivery_time")
    private Short maxDeliveryTime;
}
