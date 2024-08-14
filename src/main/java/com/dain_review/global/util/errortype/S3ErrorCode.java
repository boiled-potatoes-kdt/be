package com.dain_review.global.util.errortype;


import com.dain_review.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3ErrorCode implements ErrorCode {
    IMAGE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "파일 업로드 실패");

    private final HttpStatus status;
    private final String msg;
}
