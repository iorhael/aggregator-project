package com.senla.aggregator.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO representing basic user profile information")
public class UserProfileDto {

    @Schema(description = "Username of the user", example = "johndoe")
    @JsonProperty("username")
    private String username;

    @Schema(description = "Email address of the user", example = "johndoe@example.com")
    @JsonProperty("email")
    private String email;

    @Schema(description = "First name of the user", example = "John")
    @JsonProperty("first_name")
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    @JsonProperty("last_name")
    private String lastName;
}
