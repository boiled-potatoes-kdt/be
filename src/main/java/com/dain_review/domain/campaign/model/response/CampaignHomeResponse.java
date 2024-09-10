package com.dain_review.domain.campaign.model.response;

import java.util.List;

public record CampaignHomeResponse(
        List<CampaignSummaryResponse> premium,
        List<CampaignSummaryResponse> popular,
        List<CampaignSummaryResponse> newest,
        List<CampaignSummaryResponse> imminent
) { }
