package com.dain_review.domain.application.exception;


import com.dain_review.global.exception.ErrorCode;
import com.dain_review.global.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "체험단 신청 에러")
public class ApplicationException extends GlobalException {

    private final ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    @Override
    public void exceptionHandling() {
        log.error(errorCode.getMsg() + " : " + errorCode.getStatus().value());
    }
}
