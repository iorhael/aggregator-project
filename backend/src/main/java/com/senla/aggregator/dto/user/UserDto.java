package com.senla.aggregator.dto.user;

import com.senla.aggregator.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private Role role;
}
