package com.dain_review.domain.campaign.model.response;

import com.dain_review.domain.campaign.model.entity.Campaign;

import java.time.LocalDateTime;

public record CampaignPreviewResponse(
        Long id,
        String name,
        String reward,
        String region1,
        String region2,
        Integer point,
        String type,
        String platform,
        String label,
        Integer capacity,
        Integer applicant,
        String campaignImage,
        LocalDateTime applicationStartDate,
        LocalDateTime applicationEndDate,
        LocalDateTime experienceStartDate,
        LocalDateTime experienceEndDate,
        boolean isLike
) {
    public static CampaignPreviewResponse from(Campaign campaign) {
        return new CampaignPreviewResponse(
                campaign.getId(),
                campaign.getName(),
                campaign.getReward(),
                campaign.getRegion1(),
                campaign.getRegion2(),
                campaign.getPoint(),
                campaign.getType().getDisplayName(),
                campaign.getPlatform().getDisplayName(),
                campaign.getLabel().getDisplayName(),
                campaign.getCapacity(),
                campaign.getApplicant(),
                campaign.getCampaignImage(),
                campaign.getApplicationStartDate(),
                campaign.getApplicationEndDate(),
                campaign.getExperienceStartDate(),
                campaign.getExperienceEndDate(),
                false
        );
    }
}
