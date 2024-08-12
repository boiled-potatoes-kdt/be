package com.dain_review.domain.post.model.entity;

import com.dain_review.domain.post.model.type.CategoryType;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class Category extends BaseEntity {

	@Enumerated(EnumType.STRING)
	private CategoryType type;
}
