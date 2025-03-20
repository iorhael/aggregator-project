package com.senla.aggregator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public class ResponseInfoDto {

    @Builder.Default
    @JsonProperty("timestamp")
    private final Instant timestamp = Instant.now();

    @JsonProperty("message")
    private final String message;

    @Builder.Default
    @JsonProperty("details")
    private final Map<String, String> details = new HashMap<>();
}
