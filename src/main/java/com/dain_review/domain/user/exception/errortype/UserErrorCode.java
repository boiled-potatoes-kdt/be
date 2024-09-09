package com.dain_review.domain.user.exception.errortype;


import com.dain_review.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String msg;
}
