package com.senla.aggregator.controller.helper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ContentTypeConverter implements Converter<String, ContentType> {

    @Override
    public ContentType convert(String source) {
        return ContentType.valueOf(source.toUpperCase());
    }
}
