package com.dain_review.domain.application.model.response;


import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import java.util.List;

public record ApplicantResponse(
        Long userId,
        String name,
        Boolean selected, // 선정됐는지 여부
        Long cancelledApplicationCount, // 취소한 신청수
        String message, // 신청 한마디
        Integer capacity // 모집 인원
        ) {

    public static List<ApplicantResponse> from(Campaign campaign, Long userId) {

        if (!userId.equals(campaign.getUser().getId())) {
            throw new CampaignException(CampaignErrorCode.CAMPAIGN_NOT_FOUND);
        }

        if (!CampaignState.RECRUITING.equals(campaign.getCampaignState())) {
            throw new CampaignException(CampaignErrorCode.CANNOT_MANAGE_CAMPAIGNSTATE);
        }

        return campaign.getApplicationList().stream().map(ApplicantResponse::from).toList();
    }

    public static ApplicantResponse from(Application application) {

        // 선정됐는지 여부
        Long userId = application.getUser().getId();
        Boolean selected =
                application.getCampaign().getSelectList().stream()
                        .anyMatch(select -> userId.equals(select.getUser().getId()));

        // 취소한 캠페인 수
        Long cancelledApplicationCount =
                application.getUser().getApplicationList().stream()
                        .filter(Application::getIsDeleted)
                        .count();

        return new ApplicantResponse(
                application.getUser().getId(),
                application.getUser().getName(),
                selected,
                cancelledApplicationCount,
                application.getMessage(),
                application.getCampaign().getCapacity());
    }
}
