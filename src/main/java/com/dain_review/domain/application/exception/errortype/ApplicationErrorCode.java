package com.dain_review.domain.application.exception.errortype;


import com.dain_review.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {
    FAIL_CANCEL(HttpStatus.BAD_REQUEST, "리뷰마감 단계에서는 취소할 수 없습니다."),
    NOT_FOUND_BY_ID_USERID(HttpStatus.BAD_REQUEST, "해당 (id, userId) 의 신청이 없습니다.");

    private final HttpStatus status;
    private final String msg;
}
