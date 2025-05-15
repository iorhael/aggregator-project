package com.senla.aggregator.validation;

import com.senla.aggregator.controller.helper.ContentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AllowedFileTypesValidator implements ConstraintValidator<AllowedFileTypes, MultipartFile> {

    private List<String> allowedFileTypes;

    @Override
    public void initialize(AllowedFileTypes constraintAnnotation) {
        allowedFileTypes = Arrays.stream(constraintAnnotation.allowedFileTypes())
                .map(ContentType::getValue)
                .toList();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (Objects.isNull(file) || file.isEmpty()) return false;

        return allowedFileTypes.contains(file.getContentType());
    }
}
