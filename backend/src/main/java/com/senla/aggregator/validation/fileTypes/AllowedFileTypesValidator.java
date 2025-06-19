package com.senla.aggregator.validation.fileTypes;

import com.senla.aggregator.controller.helper.ContentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AllowedFileTypesValidator implements ConstraintValidator<AllowedFileTypes, MultipartFile> {

    private int maxFileSize;
    private List<String> allowedFileTypes;

    @Override
    public void initialize(AllowedFileTypes constraintAnnotation) {
        maxFileSize = constraintAnnotation.maxFileSize();
        allowedFileTypes = Arrays.stream(constraintAnnotation.allowedFileTypes())
                .map(ContentType::getValue)
                .toList();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (Objects.isNull(file)) return true; // RequestPart must specify should there be a file. Condition for optional file upload.
        if (file.isEmpty()) return false;

        boolean isFileSizeValid = true;
        if (maxFileSize != -1) isFileSizeValid = file.getSize() <= maxFileSize * 1024L;

        return isFileSizeValid && allowedFileTypes.contains(file.getContentType());
    }
}
