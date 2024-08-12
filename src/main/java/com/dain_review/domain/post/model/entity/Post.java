package com.dain_review.domain.post.model.entity;

import com.dain_review.domain.post.model.type.CommunityType;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Post extends BaseEntity {

	private Long userId;
	private Long categoryId;
	private String title;
	private String content;
	@Enumerated(EnumType.STRING)
	private CommunityType communityType;
	private Integer viewCount;
	private Integer commentCount;

}
