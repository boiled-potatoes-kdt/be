package com.dain_review.global.type;

import com.dain_review.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public enum RedisPrefixType {

    DEFAULT("default:")
    ;
    private final String prefix;

    @Override
    public String toString() {
        return prefix;
    }

}
