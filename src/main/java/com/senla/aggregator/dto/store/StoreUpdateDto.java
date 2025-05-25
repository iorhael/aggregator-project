package com.senla.aggregator.dto.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.ValidationPatterns;
import com.senla.aggregator.model.StoreSchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO used to update an existing store")
public class StoreUpdateDto {

    @Schema(description = "New address for the store", example = "789 Oak Avenue")
    private String address;

    @Schema(
            description = "Updated phone number in digits only, 11–15 characters",
            example = "98765432100"
    )
    @Pattern(regexp = ValidationPatterns.ONLY_NUMBERS,
            message = ValidationPatterns.ONLY_NUMBERS_MESSAGE)
    @Size(min = 11, max = 15)
    @JsonProperty("phone")
    private String phone;

    @Schema(description = "Updated store schedule")
    @Valid
    private StoreSchedule schedule;
}
