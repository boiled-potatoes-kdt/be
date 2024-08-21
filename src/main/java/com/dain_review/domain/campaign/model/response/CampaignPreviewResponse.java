package com.dain_review.domain.campaign.model.response;

import com.dain_review.domain.campaign.model.entity.Campaign;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignPreviewResponse {
    private Long id;
    private String name;
    private String reward;
    private String region1;
    private String region2;
    private Integer point;
    private String type;
    private String platform;
    private String label;
    private Integer capacity;
    private Integer applicant;
    private String campaignImage;
    private LocalDateTime applicationStartDate;
    private LocalDateTime applicationEndDate;
    private LocalDateTime experienceStartDate;
    private LocalDateTime experienceEndDate;
    private boolean isLike;

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
