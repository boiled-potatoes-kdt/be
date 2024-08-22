package com.dain_review.domain.campaign.model.response;

import java.util.List;

public record CampaignHomeResponse(
        List<CampaignPreviewResponse> premium,
        List<CampaignPreviewResponse> popular,
        List<CampaignPreviewResponse> newest,
        List<CampaignPreviewResponse> imminent
) { }