package com.senla.aggregator.dto.user;

import com.senla.aggregator.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPromotionDto {
    @NotNull
    private Role role;
}
