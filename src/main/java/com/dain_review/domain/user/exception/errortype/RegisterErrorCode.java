package com.dain_review.domain.user.exception.errortype;

import com.dain_review.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RegisterErrorCode implements ErrorCode {
    EMAIL_SAME(HttpStatus.BAD_REQUEST, "이메일이 중복되는 값이 존재합니다."),
    FAIL_IMP_ID(HttpStatus.BAD_REQUEST, "잘못된 ImpID 입니다."),
    FAIL_IMP_NAME_NOT_SAME(HttpStatus.BAD_REQUEST, "유효하지 않는 이름입니다.");


    private final HttpStatus status;
    private final String msg;
}
