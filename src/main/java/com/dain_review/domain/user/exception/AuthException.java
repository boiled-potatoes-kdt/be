package com.dain_review.domain.user.exception;

import com.dain_review.global.exception.ErrorCode;
import com.dain_review.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "인증 에러")
public class AuthException extends GlobalException {

    private final ErrorCode errorCode;

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    @Override
    public void exceptionHandling() {
        log.error(errorCode.getMsg() + " : " + errorCode.getStatus().value());
    }

}
