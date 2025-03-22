package com.senla.aggregator.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateDto {

    @NotBlank
    @Size(min = 6, max = 32)
    @JsonProperty("old_password")
    private String oldPassword;

    @NotBlank
    @Size(min = 6, max = 32)
    @JsonProperty("new_password")
    private String newPassword;
}
