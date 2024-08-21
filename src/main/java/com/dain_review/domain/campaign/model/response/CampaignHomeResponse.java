package com.dain_review.domain.campaign.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignHomeResponse {
    private List<CampaignPreviewResponse> premium;
    private List<CampaignPreviewResponse> popular;
    private List<CampaignPreviewResponse> newest;
    private List<CampaignPreviewResponse> imminent;
}
