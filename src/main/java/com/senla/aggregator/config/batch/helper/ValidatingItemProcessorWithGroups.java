package com.senla.aggregator.config.batch.helper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Slf4j(topic = "batchLogger")
public class ValidatingItemProcessorWithGroups<T> implements ItemProcessor<T, T> {

    private final Validator validator;

    private final Class<?>[] validationGroups;

    private boolean filter = false;

    public ValidatingItemProcessorWithGroups(Class<?>... validationGroups) {
        try (LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean()) {
            localValidatorFactoryBean.afterPropertiesSet();
            this.validator = localValidatorFactoryBean.getValidator();
        }

        Set<Class<?>> groupSet = new LinkedHashSet<>();
        groupSet.add(Default.class);
        if (Objects.nonNull(validationGroups)) Collections.addAll(groupSet, validationGroups);

        this.validationGroups = groupSet.toArray(new Class<?>[0]);
    }

    @Nullable
    @Override
    public T process(@NonNull T item) throws ValidationException {
        try {
            Set<ConstraintViolation<T>> constraints = validator.validate(item, validationGroups);

            if (!constraints.isEmpty() && filter) {
                log.warn("Item was skipped: {}. Reason: {}", item, createErrorMessage(constraints));
                return null;
            } else if (!constraints.isEmpty()) {
                throw new ValidationException(createErrorMessage(constraints));
            }
        } catch (ValidationException e) {
            if (filter) {
                return null;
            } else {
                throw e;
            }
        }
        return item;
    }

    private String createErrorMessage(Set<ConstraintViolation<T>> constraints) {
        return constraints.stream()
                .map(i -> String.format("%s: %s; ", i.getPropertyPath(), i.getMessage()))
                .collect(Collectors.joining());
    }
}
