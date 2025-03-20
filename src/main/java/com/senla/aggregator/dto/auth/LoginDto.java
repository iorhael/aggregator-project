package com.senla.aggregator.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDto {

    @NotBlank
    @Pattern(regexp = ValidationPatterns.USERNAME,
            message = ValidationPatterns.USERNAME_MESSAGE)
    @JsonProperty("username")
    private String username;

    @NotBlank
    @Size(min = 6, max = 32)
    @JsonProperty("password")
    private String password;
}
