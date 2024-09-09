package com.dain_review.domain.like.exception;


import com.dain_review.global.exception.ErrorCode;
import com.dain_review.global.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "좋아요 에러")
public class LikeException extends GlobalException {

    private final ErrorCode errorCode;

    public LikeException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    @Override
    public void exceptionHandling() {
        log.error(errorCode.getMsg() + " : " + errorCode.getStatus().value());
    }
}
