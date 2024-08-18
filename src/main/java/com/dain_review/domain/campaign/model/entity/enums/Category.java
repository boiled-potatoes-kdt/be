package com.dain_review.domain.campaign.model.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    A("맛집"),
    B("뷰티"),
    C("여행"),
    D("문화"),
    E("식품"),
    F("생활"),
    G("디지털");

    private final String displayName;
}
