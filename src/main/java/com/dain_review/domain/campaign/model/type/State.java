package com.dain_review.domain.campaign.model.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum State {
    A("검수증"),
    B("모집중"),
    C("모집완료"),
    D("리뷰마감");

    private final String displayName;
}
