package com.dain_review.domain.campaign.model.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Label {
    DAINREVIEW("다인체험단"),
    PREMIUM("프리미엄"),
    COMMON("일반");

    private final String displayName;
}
