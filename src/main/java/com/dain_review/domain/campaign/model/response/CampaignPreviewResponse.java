package com.dain_review.domain.campaign.model.response;


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
) { }
