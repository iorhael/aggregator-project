package com.senla.aggregator.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {

    @NotNull
    @Pattern(regexp = ValidationPatterns.USERNAME,
            message = ValidationPatterns.USERNAME_MESSAGE)
    @JsonProperty("username")
    private String username;

    @NotNull
    @Pattern(regexp = ValidationPatterns.EMAIL,
            message = ValidationPatterns.EMAIL_MESSAGE)
    @JsonProperty("email")
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @NotBlank
    @Size(min = 6, max = 32)
    @JsonProperty("password")
    private String password;
}
