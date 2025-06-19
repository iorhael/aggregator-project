package com.senla.aggregator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for password confirmation")
public class PasswordDto {

    @Schema(description = "User's current password", example = "password")
    @NotBlank
    @Size(min = 6, max = 32)
    @JsonProperty("password")
    private String password;
}
