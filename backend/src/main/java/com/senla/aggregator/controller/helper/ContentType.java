package com.senla.aggregator.controller.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    CSV("text/csv", "csv"),
    JSON("application/json", "json"),
    XML("text/xml", "xml"),
    PLAIN_TEXT("text/plain", "txt"),
    PNG("image/png", "png"),
    JPEG("image/jpeg", "jpeg");

    private final String value;
    private final String fileExtension;

    public static ContentType fromValue(String value) {
        for (ContentType type : values()) {
            if (type.value.equalsIgnoreCase(value)) return type;
        }
        throw new IllegalArgumentException("No enum constant: " + value);
    }
}
