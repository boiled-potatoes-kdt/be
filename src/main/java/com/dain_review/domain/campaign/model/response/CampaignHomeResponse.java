package com.dain_review.domain.campaign.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CampaignHomeResponse(
        List<CampaignPreviewResponse> premium,
        List<CampaignPreviewResponse> popular,
        List<CampaignPreviewResponse> newest,
        List<CampaignPreviewResponse> imminent
) { }