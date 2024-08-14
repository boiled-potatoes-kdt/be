package com.dain_review.global.validation;


import com.dain_review.global.validation.type.ImageFileValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileValidator implements ConstraintValidator<ImageFileValidation, MultipartFile> {

    private final List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return file != null
                && !file.isEmpty()
                && file.getOriginalFilename() != null
                && allowedExtensions.stream()
                        .map(String::toLowerCase)
                        .anyMatch(
                                ext ->
                                        ext.equalsIgnoreCase(
                                                getFileExtension(file.getOriginalFilename())));
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }
}
