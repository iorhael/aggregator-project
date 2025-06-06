package com.senla.aggregator.validation.contentTypes;

import com.senla.aggregator.controller.helper.ContentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AllowedContentTypesValidator implements ConstraintValidator<AllowedContentTypes, ContentType> {

    private List<ContentType> allowedContentTypes;

    @Override
    public void initialize(AllowedContentTypes constraintAnnotation) {
        allowedContentTypes = Arrays.stream(constraintAnnotation.allowedContentTypes()).toList();
    }

    @Override
    public boolean isValid(ContentType contentType, ConstraintValidatorContext context) {
        if (Objects.isNull(contentType)) return false;
        return allowedContentTypes.contains(contentType);
    }
}
