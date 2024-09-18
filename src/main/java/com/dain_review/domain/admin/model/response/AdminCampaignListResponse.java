package com.dain_review.domain.admin.model.response;


import com.dain_review.domain.campaign.model.entity.Campaign;
import java.time.format.DateTimeFormatter;

public record AdminCampaignListResponse(
        String id,
        String title,
        String label,
        Integer point,
        Integer capacity,
        String email,
        String businessName,
        String platform,
        String type,
        String phone,
        String createdAt,
        String campaignState) {

    public static AdminCampaignListResponse from(Campaign campaign) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new AdminCampaignListResponse(
                String.valueOf(campaign.getId()),
                campaign.getBusinessName(),
                campaign.getLabel().getDisplayName(),
                campaign.getTotalPoints(),
                campaign.getCapacity(),
                campaign.getUser().getEmail(),
                campaign.getUser().getEnterpriser().getCompany(),
                campaign.getPlatform().getDisplayName(),
                campaign.getType().getDisplayName(),
                campaign.getUser().getPhone(),
                formatter.format(campaign.getCreatedAt()),
                campaign.getCampaignState().getDisplayName());
    }
}
