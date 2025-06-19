package com.senla.aggregator.controller.helper;

import com.senla.aggregator.model.UserTag;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserTagConverter implements Converter<String, UserTag> {

    @Override
    public UserTag convert(String source) {
        return UserTag.valueOf(source.toUpperCase());
    }
}
