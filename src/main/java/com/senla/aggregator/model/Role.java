package com.senla.aggregator.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    DEFAULT("DEFAULT_ROLE"),
    AUTHOR("AUTHOR_ROLE"),
    RETAILER("RETAILER_ROLE"),
    ADMIN("ADMIN_ROLE");

   private final String prefixedRole;
}
