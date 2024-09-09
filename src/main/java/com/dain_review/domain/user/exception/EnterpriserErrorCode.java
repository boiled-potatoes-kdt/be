package com.dain_review.domain.user.exception;


import com.dain_review.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum EnterpriserErrorCode implements ErrorCode {
    ;

    @Override
    public HttpStatus getStatus() {
        return null;
    }

    @Override
    public String getMsg() {
        return null;
    }
}
