package com.dain_review.domain.user.exception;


import com.dain_review.global.exception.ErrorCode;
import com.dain_review.global.exception.GlobalException;

public class UserException extends GlobalException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public void exceptionHandling() {}
}
