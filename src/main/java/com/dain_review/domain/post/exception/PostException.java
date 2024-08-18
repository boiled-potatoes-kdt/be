package com.dain_review.domain.post.exception;


import com.dain_review.global.exception.ErrorCode;
import com.dain_review.global.exception.GlobalException;

public class PostException extends GlobalException {

    public PostException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public void exceptionHandling() {
        // 예외 처리 로직을 추가할 수 있습니다. 예를 들어, 로깅.
    }
}
