package com.senla.aggregator.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidImageValidator.class)
public @interface ValidImage {
    String message() default "File is not an image";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
