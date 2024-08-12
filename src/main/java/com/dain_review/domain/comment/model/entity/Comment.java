package com.dain_review.domain.comment.model.entity;

import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
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

	private Long userId;
	private Long postId;
	private Long subCommentId;
	private String content;

}
