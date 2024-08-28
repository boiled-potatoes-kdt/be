package com.dain_review.domain.campaign.model.request;


import com.dain_review.domain.campaign.model.entity.enums.Platform;
import com.dain_review.domain.campaign.model.entity.enums.State;

public record CampaignFilterRequest(Platform platform, State state, String keyword) {}
