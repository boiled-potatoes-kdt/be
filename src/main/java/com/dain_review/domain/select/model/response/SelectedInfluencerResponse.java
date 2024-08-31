package com.dain_review.domain.select.model.response;


import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import java.util.List;

public record SelectedInfluencerResponse(String name, String phone, String message) {

    public static List<SelectedInfluencerResponse> from(Campaign campaign, Long userId) {
        if (!userId.equals(campaign.getUser().getId())) {
            throw new CampaignException(CampaignErrorCode.CAMPAIGN_NOT_FOUND);
        }
        if (!CampaignState.RECRUITMENT_COMPLETED.equals(campaign.getCampaignState())) {
            throw new CampaignException(CampaignErrorCode.CANNOT_MANAGE_CAMPAIGNSTATE);
        }

        return campaign.getApplicationList().stream()
                .map(SelectedInfluencerResponse::from)
                .toList();
    }

    public static SelectedInfluencerResponse from(Application application) {

        return new SelectedInfluencerResponse(
                application.getUser().getName(),
                application.getUser().getPhone(),
                application.getMessage());
    }
}
