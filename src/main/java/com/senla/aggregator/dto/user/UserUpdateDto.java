package com.senla.aggregator.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.dto.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO used to update user profile information")
public class UserUpdateDto {

    @Schema(description = "Updated email address", example = "newemail@example.com")
    @Pattern(regexp = ValidationPatterns.EMAIL,
            message = ValidationPatterns.EMAIL_MESSAGE)
    @JsonProperty("email")
    private String email;

    @Schema(description = "Updated first name", example = "John")
    @JsonProperty("first_name")
    private String firstName;

    @Schema(description = "Updated last name", example = "Doe")
    @JsonProperty("last_name")
    private String lastName;
}
