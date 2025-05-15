package com.senla.aggregator.config.batch.helper;

import jakarta.validation.Validator;
import lombok.Setter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Setter
public class ValidatingItemProcessorWithGroups<T> implements ItemProcessor<T, T> {

    private final Validator validator;

    private final Class<?>[] validationGroups;

    private boolean filter = false;

    public ValidatingItemProcessorWithGroups(Class<?>... validationGroups) {
        try (LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean()) {
            localValidatorFactoryBean.afterPropertiesSet();
            this.validator = localValidatorFactoryBean.getValidator();
        }
        this.validationGroups = validationGroups;
    }

    @Nullable
    @Override
    public T process(@NonNull T item) throws ValidationException {
        try {
            validator.validate(item, validationGroups);
        } catch (ValidationException e) {
            if (filter) {
                return null;
            } else {
                throw e;
            }
        }
        return item;
    }
}
