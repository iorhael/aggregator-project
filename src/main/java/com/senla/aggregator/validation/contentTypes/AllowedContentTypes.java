package com.senla.aggregator.validation.contentTypes;

import com.senla.aggregator.controller.helper.ContentType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedContentTypesValidator.class)
public @interface AllowedContentTypes {
    String message() default "Incompatible content type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    ContentType[] allowedContentTypes();
}
