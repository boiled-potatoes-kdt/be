package com.dain_review.domain.comment.model.response;

import com.dain_review.domain.comment.model.entity.Comment;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long parentId,
        String userName,
        String userProfileImage,
        String content,
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment, String nickName, String imageUrl) {
        Long parentId = null;
        if (comment.getParent()!=null)
            parentId = comment.getParent().getId();
        User user = comment.getUser();

        return new CommentResponse(
                comment.getId(),
                parentId,
                nickName,
                imageUrl,
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
