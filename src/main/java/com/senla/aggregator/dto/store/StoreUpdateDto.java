package com.senla.aggregator.dto.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.ValidationPatterns;
import com.senla.aggregator.model.StoreSchedule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreUpdateDto {

    private String address;

    @Pattern(regexp = ValidationPatterns.ONLY_NUMBERS,
            message = ValidationPatterns.ONLY_NUMBERS_MESSAGE)
    @Size(min = 11, max = 15)
    @JsonProperty("phone")
    private String phone;

    @Valid
    private StoreSchedule schedule;
}
