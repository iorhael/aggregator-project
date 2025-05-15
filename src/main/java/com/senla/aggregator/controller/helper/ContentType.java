package com.senla.aggregator.controller.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    CSV("text/csv"),
    JSON("application/json"),
    XML("text/xml");

    private final String value;

    public static ContentType fromValue(String value) {
        for (ContentType type : values()) {
            if (type.value.equalsIgnoreCase(value)) return type;
        }
        throw new IllegalArgumentException("No enum constant: " + value);
    }
}
