package com.dain_review.domain.application.exception;


import com.dain_review.global.exception.ErrorCode;
import com.dain_review.global.exception.GlobalException;

public class ApplicationException extends GlobalException {

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public void exceptionHandling() {}
}
