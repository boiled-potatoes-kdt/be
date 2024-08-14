package com.dain_review.global.validation;

import com.dain_review.global.validation.type.ImageFileValidation;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ImageFileValidator implements ConstraintValidator<ImageFileValidation, MultipartFile> {

    private final List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return file != null && !file.isEmpty() && // 파일이 null 또는 empty거나 파일 이름이 null이면 false 반환
                file.getOriginalFilename() != null &&
                allowedExtensions.stream() // 파일의 확장자가 허용된 확장자 목록에 있는지 확인
                        .anyMatch(ext -> ext.equalsIgnoreCase(getFileExtension(file.getOriginalFilename())));
    }

    private String getFileExtension(String fileName) { // 파일 이름 '.' 뒤에 오는 문자열 파악
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }
}
