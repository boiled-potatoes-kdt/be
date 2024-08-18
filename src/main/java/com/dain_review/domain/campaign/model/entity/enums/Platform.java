package com.dain_review.domain.campaign.model.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Platform {
    A("블로그"),
    B("인스타그램"),
    C("유튜브"),
    D("틱톡"),
    E("릴스"),
    F("쇼츠");

    private final String displayName;
}
