package com.dain_review.global.util.error;


import com.dain_review.global.exception.GlobalException;
import com.dain_review.global.util.errortype.S3ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class S3Exception extends GlobalException {
    private final S3ErrorCode errorCode;

    public S3Exception(S3ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    @Override
    public void exceptionHandling() {
        log.info(errorCode.getStatus() + ": " + errorCode.getMsg());
    }
}
