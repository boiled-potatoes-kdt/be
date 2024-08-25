package com.dain_review.domain.campaign.model.entity;


import com.dain_review.domain.campaign.model.entity.enums.Category;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import com.dain_review.domain.campaign.model.entity.enums.State;
import com.dain_review.domain.campaign.model.entity.enums.Type;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
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

    @ManyToOne private User user;
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

    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    List<AvaliableDay> avaliableDayList;

    private Integer capacity;
    private Integer applicant;
    private Integer likeCount;
    private String label;
    private String campaignImage;
    private String reward;

    private String notation;
    private String information;
    private String requirement;
    private Boolean today;
    private LocalDateTime approvalDate;
    private LocalDateTime applicationStartDate;
    private LocalDateTime applicationEndDate;
    private LocalDateTime announcementDate;
    private LocalDateTime experienceStartDate;
    private LocalDateTime experienceEndDate;
    private LocalDateTime reviewDate;
    private Boolean isDeleted;
}
