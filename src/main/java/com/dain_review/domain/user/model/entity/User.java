package com.dain_review.domain.user.model.entity;

import com.dain_review.domain.user.model.type.Role;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

	private String name;
	private String email;
	private String password;
	private Boolean status;
	@Enumerated(EnumType.STRING)
	private Role role;

	private Long point;
	private String phone;
	private String joinPath;
	private String address;
	private String profileImage;
	private String marketing;
	private Boolean penalty;


}
