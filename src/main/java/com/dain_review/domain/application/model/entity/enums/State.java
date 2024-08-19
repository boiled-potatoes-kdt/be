package com.dain_review.domain.application.model.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum State {
    /** 2차목표 내용 */
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거부");

    private final String displayName;
}
