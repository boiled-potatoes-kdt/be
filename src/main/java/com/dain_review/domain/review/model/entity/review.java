package com.dain_review.domain.review.model.entity;

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
public class review extends BaseEntity {

	private Long userId;
	private Long campaignId;
	private Long applicationId;
	private String url;

}
