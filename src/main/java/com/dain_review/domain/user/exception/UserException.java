package com.dain_review.domain.user.exception;

import com.dain_review.global.exception.ErrorCode;
import com.dain_review.global.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserException extends GlobalException {

    private final ErrorCode errorCode;

    public UserException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    @Override
    public void exceptionHandling() {
        log.info(errorCode.getStatus() + ": " + errorCode.getMsg());
    }
}
