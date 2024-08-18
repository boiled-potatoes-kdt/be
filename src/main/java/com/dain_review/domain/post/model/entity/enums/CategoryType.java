package com.dain_review.domain.post.model.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {
    A("공지사항"),
    B("커뮤니티"),
    C("맞팔/서이추");

    private final String displayName;
}
