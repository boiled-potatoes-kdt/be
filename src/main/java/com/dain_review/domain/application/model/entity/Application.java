package com.dain_review.domain.application.model.entity;


import com.dain_review.domain.application.model.entity.enums.State;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Duration;
import java.time.LocalDateTime;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    private String message;

    @Enumerated(EnumType.STRING)
    private State state;

    private Boolean isDeleted;

    public void delete() {
        this.isDeleted = true;
    }

    public CampaignResponse toCampaignResponse() {

        // 지원 마감까지 남은 일수
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, this.campaign.getApplicationEndDate());
        Long applicationDeadline = Math.max(duration.toDays(), 0);

        // 취소가능한지 여부 - 모집중, 모집완료, 체험&리뷰 단계에서만 가능
        Boolean isCancel = !CampaignState.REVIEW_CLOSED.equals(this.campaign.getCampaignState());

        // 찜한 캠페인인지
        // Todo - 이부분을 어떻게 구현해야할지
        //        this.campaign.getLikeList().

        // CampaignResponse 로 변환
        return new CampaignResponse(
                this.campaign.getId(),
                this.campaign.getUser().getNickname(),
                this.campaign.getReward(),
                this.campaign.getRegion1(),
                this.campaign.getRegion2(),
                this.campaign.getType(),
                this.campaign.getPlatform(),
                this.campaign.getUser().getProfileImage(),
                this.campaign.getCampaignState(),
                this.campaign.getCapacity(),
                this.campaign.getApplicant(),
                this.campaign.getExperienceStartDate(),
                this.campaign.getExperienceEndDate(),
                applicationDeadline,
                isCancel,
                this.campaign.getLabel(),
                false);
    }
}
