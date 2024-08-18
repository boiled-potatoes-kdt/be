package com.dain_review.domain.post.model.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunityType {
    QnA("질문하기"),
    KNOWHOW("노하우"),
    COMPANION("동행"),
    ETC("기타");

    private final String displayName;
}
