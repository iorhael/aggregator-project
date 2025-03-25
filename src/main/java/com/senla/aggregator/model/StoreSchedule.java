package com.senla.aggregator.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreSchedule {

    @NotBlank
    private String monday;

    @NotBlank
    private String tuesday;

    @NotBlank
    private String wednesday;

    @NotBlank
    private String thursday;

    @NotBlank
    private String friday;

    @NotBlank
    private String saturday;

    @NotBlank
    private String sunday;
}
