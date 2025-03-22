package com.senla.aggregator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDto {

    @NotBlank
    @Size(min = 6, max = 32)
    @JsonProperty("password")
    private String password;
}
