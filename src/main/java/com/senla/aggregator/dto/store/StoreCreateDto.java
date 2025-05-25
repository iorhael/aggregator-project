package com.senla.aggregator.dto.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.ValidationPatterns;
import com.senla.aggregator.model.StoreSchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO used to create a new store")
public class StoreCreateDto {

    @Schema(
            description = "Address of the store",
            example = "456 Elm Street"
    )
    @NotBlank
    @JsonProperty("address")
    private String address;

    @Schema(
            description = "Phone number in digits only, 11–15 characters",
            example = "12345678901"
    )
    @NotBlank
    @Pattern(regexp = ValidationPatterns.ONLY_NUMBERS,
            message = ValidationPatterns.ONLY_NUMBERS_MESSAGE)
    @Size(min = 11, max = 15)
    @JsonProperty("phone")
    private String phone;

    @Schema(description = "Opening hours for the store")
    @Valid
    @JsonProperty("opening_hours")
    private StoreSchedule openingHours;
}
