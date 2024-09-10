package com.dain_review.domain.campaign.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CampaignHomeResponse(
        List<CampaignSummaryResponse> premium,
        List<CampaignSummaryResponse> popular,
        List<CampaignSummaryResponse> newest,
        List<CampaignSummaryResponse> imminent
) { }