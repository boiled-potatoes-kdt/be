package com.dain_review.domain.campaign.model.entity;


import com.dain_review.domain.campaign.model.type.Category;
import com.dain_review.domain.campaign.model.type.Platform;
import com.dain_review.domain.campaign.model.type.State;
import com.dain_review.domain.campaign.model.type.Type;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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

    @ManyToOne private User user; // 하나의 사업주에 여러 개의 캠페인
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

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    List<AvaliableDay> avaliableDayList; // 하나의 캠페인의 여러 개의 가능한 요일

    private Integer capacity;
    private Integer applicant;
    private Integer likeCount;
    private String campaignImage;
    private String reward;

    private String notation;
    private String information;
    private String requirement;
    private Boolean today;
    private LocalDateTime applicationStartDate;
    private LocalDateTime applicationEndDate;
    private LocalDateTime announcementDate;
    private LocalDateTime experienceStartDate;
    private LocalDateTime experienceEndDate;
    private LocalTime experienceStarrTime;
    private LocalTime experienceEndTime;
    private LocalDateTime reviewDate;

    private State state;
}
