package com.dain_review.domain.comment.model.request;

public record CommentRequest(
        Long id, Long postId, Long parentId, String content
){ }
