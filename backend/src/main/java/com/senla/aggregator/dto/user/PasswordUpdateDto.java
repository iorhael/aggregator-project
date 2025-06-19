package com.senla.aggregator.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO used to update user password")
public class PasswordUpdateDto {

    @Schema(description = "Current password", example = "password")
    @NotBlank
    @Size(min = 6, max = 32)
    @JsonProperty("old_password")
    private String oldPassword;

    @Schema(description = "New password", example = "password1")
    @NotBlank
    @Size(min = 6, max = 32)
    @JsonProperty("new_password")
    private String newPassword;
}
