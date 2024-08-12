package com.dain_review.domain.campaign.model.entity;

import com.dain_review.domain.campaign.model.type.Category;
import com.dain_review.domain.campaign.model.type.Platform;
import com.dain_review.domain.campaign.model.type.State;
import com.dain_review.domain.campaign.model.type.Type;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Campaign extends BaseEntity {

	private Long userId;
	private String name;
	private Double latitude;
	private Double longitude;
	private String region1;
	private String region2;

	@Enumerated(EnumType.STRING)
	private Type type;
	@Enumerated(EnumType.STRING)
	private Category category;
	@Enumerated(EnumType.STRING)
	private Platform platform;

	private Integer capacity;
	private Integer applicant;
	private Integer likeCount;
	private String campaignImage;
	private String reward;

	private String notation;
	private String information;
	private String requirement;
	private Boolean today;
	private LocalDate applicationStartDate;
	private LocalDate applicationEndDate;
	private LocalDate announcementDate;
	private LocalDate experienceStartDate;
	private LocalDate experienceEndDate;
	private LocalDate experienceStarrTime;
	private LocalDate experienceEndTime;
	private LocalDate reviewDate;

	private State state;

	private Boolean monday;
	private Boolean tuesday;
	private Boolean wednesday;
	private Boolean thursday;
	private Boolean friday;
	private Boolean saturday;
	private Boolean sunday;
	private String tag1;
	private String tag2;
	private String tag3;






}
