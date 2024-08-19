package com.dain_review.domain.campaign.model.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum State {
    INSPECTION("검수증"),
    RECRUITING("모집중"),
    RECRUITMENT_COMPLETED("모집완료"),
    REVIEW_CLOSED("리뷰마감");

    private final String displayName;
}
