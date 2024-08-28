package com.dain_review.domain.application.model.entity;


import com.dain_review.domain.application.model.entity.enums.ApplicationState;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.State;
import com.dain_review.domain.campaign.model.response.ApplicationCampaignResponse;
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
    private ApplicationState applicationState;

    private Boolean isDeleted;

    public void delete() {
        this.isDeleted = true;
    }

    public ApplicationCampaignResponse toApplicationCampaignResponse() {
        // 지원 마감까지 남은 일수 계산
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, this.campaign.getApplicationEndDate());
        Long applicationDeadline = Math.max(duration.toDays(), 0);

        // 취소 가능 여부 판단 - 리뷰 종료 단계가 아닌 경우
        Boolean isCancel = !State.REVIEW_CLOSED.equals(this.campaign.getState());

        // CampaignToResponse 객체로 변환
        return new ApplicationCampaignResponse(
                this.campaign.getId(),
                this.campaign.getUser().getNickname(),
                this.campaign.getServiceProvided(),
                this.campaign.getCity(),
                this.campaign.getDistrict(),
                this.campaign.getType(),
                this.campaign.getPlatform(),
                this.campaign.getUser().getProfileImage(),
                this.campaign.getState(),
                this.campaign.getCapacity(),
                this.campaign.getCurrentApplicants(),
                this.campaign.getExperienceStartDate(),
                this.campaign.getExperienceEndDate(),
                applicationDeadline,
                isCancel,
                this.campaign.getLabel(),
                false);
    }
}
