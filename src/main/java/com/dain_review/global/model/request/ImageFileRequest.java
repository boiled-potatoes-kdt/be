package com.dain_review.global.model.request;

import com.dain_review.global.validation.type.ImageFileValidation;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ImageFileRequest {

    @ImageFileValidation
    private MultipartFile file;
}
