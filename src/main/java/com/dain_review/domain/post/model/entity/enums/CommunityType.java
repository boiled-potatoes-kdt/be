package com.dain_review.domain.post.model.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunityType {
<<<<<<< HEAD
    QnA("질문하기"),
=======
    QUESTION("질문하기"),
>>>>>>> 37241ba30cfc18659fdaacb018cdba4b77cb38dd
    KNOWHOW("노하우"),
    COMPANION("동행"),
    ETC("기타");

    private final String displayName;
}
