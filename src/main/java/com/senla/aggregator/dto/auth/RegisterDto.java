package com.senla.aggregator.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO used to register a new user")
public class RegisterDto {

    @Schema(
            description = "Unique username (only letters, digits, underscores allowed)",
            example = "john_doe"
    )
    @NotNull
    @Pattern(regexp = ValidationPatterns.USERNAME, message = ValidationPatterns.USERNAME_MESSAGE)
    @JsonProperty("username")
    private String username;

    @Schema(description = "Valid email address", example = "john.doe@example.com")
    @NotNull
    @Pattern(regexp = ValidationPatterns.EMAIL, message = ValidationPatterns.EMAIL_MESSAGE)
    @JsonProperty("email")
    private String email;

    @Schema(description = "First name of the user", example = "John")
    @JsonProperty("first_name")
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    @JsonProperty("last_name")
    private String lastName;

    @Schema(description = "Password (6 to 32 characters)", example = "password")
    @NotBlank
    @Size(min = 6, max = 32)
    @JsonProperty("password")
    private String password;
}
