package com.dain_review.domain.campaign.model.request;


import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.entity.enums.Platform;

public record CampaignFilterRequest(
        Platform platform, CampaignState campaignState, String keyword) {}
