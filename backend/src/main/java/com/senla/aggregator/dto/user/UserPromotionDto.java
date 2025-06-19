package com.senla.aggregator.dto.user;

import com.senla.aggregator.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO used to promote a user to a specific role")
public class UserPromotionDto {

    @Schema(description = "Role to assign to the user")
    @NotNull
    private Role role;
}
