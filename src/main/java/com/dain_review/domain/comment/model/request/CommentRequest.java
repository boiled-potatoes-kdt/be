package com.dain_review.domain.comment.model.request;

public record CommentRequest(Long parentId, String content) {}
