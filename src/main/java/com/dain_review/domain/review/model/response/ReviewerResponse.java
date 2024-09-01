package com.dain_review.domain.review.model.response;


import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.review.model.entity.Review;
import java.time.LocalDate;
import java.util.List;

public record ReviewerResponse(String name, String phone, LocalDate reviewDate, String reviewUrl) {

    public static List<ReviewerResponse> from(Campaign campaign, Long userId) {
        if (!userId.equals(campaign.getUser().getId())) {
            throw new CampaignException(CampaignErrorCode.CAMPAIGN_NOT_FOUND);
        }
        if (!(CampaignState.EXPERIENCE_AND_REVIEW.equals(campaign.getCampaignState())
                || CampaignState.REVIEW_CLOSED.equals(campaign.getCampaignState()))) {
            throw new CampaignException(CampaignErrorCode.CANNOT_MANAGE_CAMPAIGNSTATE);
        }

        return campaign.getReviewList().stream().map(ReviewerResponse::from).toList();
    }

    public static ReviewerResponse from(Review review) {

        return new ReviewerResponse(
                review.getUser().getName(),
                review.getUser().getPhone(),
                review.getCreatedAt().toLocalDate(),
                review.getUrl());
    }
}
