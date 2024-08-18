package com.dain_review.domain.post.event;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostReadEvent {
    private final Long postId;
}
