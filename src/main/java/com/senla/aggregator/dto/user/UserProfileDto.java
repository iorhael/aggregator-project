package com.senla.aggregator.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;
}
