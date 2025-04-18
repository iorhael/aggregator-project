package com.senla.aggregator.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    DEFAULT("ROLE_DEFAULT"),
    AUTHOR("ROLE_AUTHOR"),
    RETAILER("ROLE_RETAILER"),
    ADMIN("ROLE_ADMIN");

    private final String prefixedRole;
}
