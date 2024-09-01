package com.dain_review.domain.application.model.entity;


import com.dain_review.domain.application.exception.ApplicationException;
import com.dain_review.domain.application.exception.errortype.ApplicationErrorCode;
import com.dain_review.domain.application.model.entity.enums.ApplicationState;
import com.dain_review.domain.application.model.request.ApplicationRequest;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    public static Application from(
            ApplicationRequest applicationRequest, User user, Campaign campaign) {
        return Application.builder()
                .user(user)
                .campaign(campaign)
                .message(applicationRequest.message())
                .applicationState(ApplicationState.PENDING)
                .isDeleted(false)
                .build();
    }

    public void delete(Long userId) {
        // 삭제하려는 신청정보의 사용자 아이디와 로그인 아이디가 일치하는지 확인
        if (!user.getId().equals(userId)) {
            throw new ApplicationException(ApplicationErrorCode.FAIL_CANCEL);
        }

        this.isDeleted = true;
    }
}
