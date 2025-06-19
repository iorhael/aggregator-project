package com.senla.aggregator.dto.productCard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for filtering product cards")
public class ProductCardFilterDto {

    @Schema(description = "Retailer name to filter by", example = "7element")
    @JsonProperty("retailer_name")
    private String retailerName;

    @Schema(description = "Minimum warranty period in months", example = "12")
    @JsonProperty("warranty")
    private Short warranty;

    @Schema(description = "Minimum installment period in months", example = "6")
    @JsonProperty("installment_period")
    private Short installmentPeriod;

    @Schema(description = "Maximum delivery time in days", example = "5")
    @JsonProperty("max_delivery_time")
    private Short maxDeliveryTime;
}
