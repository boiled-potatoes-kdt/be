package com.dain_review.domain.post.model.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunityType {
    A("질문하기"),
    B("노하우"),
    C("동행"),
    D("기타");

    private final String displayName;
}
