package com.senla.aggregator.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Login request payload")
public class LoginDto {

    @Schema(description = "Username used to log in", example = "default_user")
    @NotBlank
    @Pattern(regexp = ValidationPatterns.USERNAME,
            message = ValidationPatterns.USERNAME_MESSAGE)
    @JsonProperty("username")
    private String username;

    @Schema(description = "User's password", example = "password")
    @NotBlank
    @Size(min = 6, max = 32)
    @JsonProperty("password")
    private String password;
}
