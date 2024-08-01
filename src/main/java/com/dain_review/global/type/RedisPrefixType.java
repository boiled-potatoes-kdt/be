package com.dain_review.global.type;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RedisPrefixType {
    DEFAULT("default:");
    private final String prefix;

    @Override
    public String toString() {
        return prefix;
    }
}
