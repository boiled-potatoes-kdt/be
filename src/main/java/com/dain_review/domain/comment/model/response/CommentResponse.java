package com.dain_review.domain.comment.model.response;

import com.dain_review.domain.comment.model.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long parentId,
        String userName,
        String userProfileImage,
        String content,
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        Long parentId = null;
        if (comment.getParent()!=null)
            parentId = comment.getParent().getId();

        return new CommentResponse(
                comment.getId(),
                parentId,
                "Test User Name",
                "Test User Profile Image URL",
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
