package com.dain_review.domain.post.exception;


import com.dain_review.global.exception.ErrorCode;
import com.dain_review.global.exception.GlobalException;

public class PostException extends GlobalException {

    public PostException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public void exceptionHandling() {}
}
