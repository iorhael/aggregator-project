package com.senla.aggregator.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ValidImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (Objects.isNull(file)) return true; // RequestPart must specify should there be a file. Condition for optional file upload.
        if (file.isEmpty()) return false;

        try (InputStream stream = file.getInputStream()) {
            if (Objects.isNull(ImageIO.read(stream))) return false;
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
