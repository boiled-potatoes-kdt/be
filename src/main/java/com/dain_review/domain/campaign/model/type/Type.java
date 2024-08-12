package com.dain_review.domain.campaign.model.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {
    A("방문형"),
    B("구매형"),
    C("베송형"),
    D("기자단"),
    E("포장");

    private final String displayName;
}
