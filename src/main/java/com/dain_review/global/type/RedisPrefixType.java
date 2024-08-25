package com.dain_review.global.type;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RedisPrefixType {
    DEFAULT("default:"),
    POST_VIEW_COUNT("postViewCount:");

    private final String prefix;

    @Override
    public String toString() {
        return prefix;
    }
}
