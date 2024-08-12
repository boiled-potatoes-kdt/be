package com.dain_review.domain.user.model.entity;

import com.dain_review.domain.user.model.type.Gender;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;

public class UserInfluencer extends BaseEntity {

	private Long userId;
	private Long snsId;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	private LocalDate birthday;

}
