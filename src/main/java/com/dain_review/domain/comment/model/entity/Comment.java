package com.dain_review.domain.comment.model.entity;


import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Comment extends BaseEntity {

    @ManyToOne private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private Long subCommentId;

    private String content;
}
