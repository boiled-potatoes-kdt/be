package com.dain_review.domain.campaign.model.entity;


import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.entity.enums.Category;
import com.dain_review.domain.campaign.model.entity.enums.Label;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import com.dain_review.domain.campaign.model.entity.enums.Type;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.like.model.entity.Like;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.Duration;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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
    List<AvaliableDay> avaliableDayList;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    List<Like> likeList;

    private Integer capacity;
    private Integer applicant;
    private Long likeCount;
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

    @Enumerated(EnumType.STRING)
    private Label label;

    @Enumerated(EnumType.STRING)
    private CampaignState campaignState;

    private Boolean isDeleted;

    public CampaignResponse toCampaignResponse() {
        // 지원 마감까지 남은 일수
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, this.getApplicationEndDate());
        Long applicationDeadline = Math.max(duration.toDays(), 0);

        // 취소가능한지 여부 - 모집중, 모집완료, 체험&리뷰 단계에서만 가능
        Boolean isCancel = !CampaignState.REVIEW_CLOSED.equals(this.getCampaignState());

        // 찜한 캠페인인지
        // Todo - 이부분을 어떻게 구현해야할지
        //        this.getLikeList().

        // CampaignResponse 로 변환
        return new CampaignResponse(
                this.getId(),
                this.getUser().getNickname(),
                this.getReward(),
                this.getRegion1(),
                this.getRegion2(),
                this.getType(),
                this.getPlatform(),
                this.getUser().getProfileImage(),
                this.getCampaignState(),
                this.getCapacity(),
                this.getApplicant(),
                this.getExperienceStartDate(),
                this.getExperienceEndDate(),
                applicationDeadline,
                isCancel,
                this.getLabel(),
                false);
    }
}
