package com.dain_review.domain.user.exception;


import com.dain_review.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {
    NOT_FOUND_BY_ID(HttpStatus.BAD_REQUEST, "해당 id의 사용자가 없습니다.");

    private final HttpStatus status;
    private final String msg;
}
