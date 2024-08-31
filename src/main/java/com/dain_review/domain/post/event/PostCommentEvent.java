package com.dain_review.domain.post.event;


import com.dain_review.domain.post.model.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCommentEvent {
    private final Post post;
}
