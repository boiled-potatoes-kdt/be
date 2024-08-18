package com.dain_review.domain.application.model.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum State {
    /** 2차목표 내용 */
    A("대기"),
    B("승인"),
    C("거부");

    private final String displayName;
}
