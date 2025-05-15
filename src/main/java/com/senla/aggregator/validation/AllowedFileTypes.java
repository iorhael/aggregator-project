package com.senla.aggregator.validation;

import com.senla.aggregator.controller.helper.ContentType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedFileTypesValidator.class)
public @interface AllowedFileTypes {
    String message() default "Incompatible file type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    ContentType[] allowedFileTypes();
}
