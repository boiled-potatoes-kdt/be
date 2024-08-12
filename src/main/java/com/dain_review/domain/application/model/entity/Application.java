package com.dain_review.domain.application.model.entity;

import com.dain_review.domain.application.model.type.State;
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
public class Application extends BaseEntity {

	private Long userId;
	private Long campaignId;
	private String message;
	@Enumerated(EnumType.STRING)
	private State state;


}
