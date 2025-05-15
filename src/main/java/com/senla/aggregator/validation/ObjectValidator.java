package com.senla.aggregator.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ObjectValidator {
    private static final String VIOLATION_MESSAGE = " %s %s;";

    private final Validator validator;

    public <T> void validateList(List<T> objects) {
        for (T object : objects) {
            Set<ConstraintViolation<T>> violations = validator.validate(object);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<T> constraintViolation : violations) {
                    sb.append(String.format(VIOLATION_MESSAGE,
                            constraintViolation.getPropertyPath(),
                            constraintViolation.getMessage()));
                }
                throw new ConstraintViolationException("Error occurred:" + sb, violations);
            }
        }
    }
}
