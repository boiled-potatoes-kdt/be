package com.dain_review.domain.campaign.exception;


import com.dain_review.global.exception.ErrorCode;
import com.dain_review.global.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "체험단 에러")
public class CampaignException extends GlobalException {

    private final ErrorCode errorCode;

    public CampaignException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    @Override
    public void exceptionHandling() {
        log.error(errorCode.getMsg() + " : " + errorCode.getStatus().value());
    }
}
