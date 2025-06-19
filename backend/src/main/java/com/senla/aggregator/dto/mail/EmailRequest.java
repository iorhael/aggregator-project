package com.senla.aggregator.dto.mail;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.io.File;
import java.util.Map;

@Getter
@Builder
public class EmailRequest {

    private final String subject;

    private final String body;

    private final File attachment;

    private final String templateName;

    @Singular("modelParam")
    private final Map<String, Object> model;

    private final String[] recipients;
}
