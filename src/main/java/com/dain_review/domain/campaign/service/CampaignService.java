package com.dain_review.domain.campaign.service;


import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.request.CampaignFilterRequest;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public Campaign getCampaign(Long campaignId) {

        return campaignRepository.findById(campaignId).orElseThrow(() -> new RuntimeException());
    }

    public Object get(CampaignFilterRequest campaignFilterRequest, Pageable pageable, Long userId) {

        // 필터조건으로 Page<Application> 가져오기
        Page<Campaign> campaignPage =
                campaignRepository.findByStateAndPlatformAndNameContainingAndUserId(
                        campaignFilterRequest.campaignState(),
                        campaignFilterRequest.platform(),
                        campaignFilterRequest.keyword(),
                        userId,
                        pageable);

        // Page<Campaign> -> Page<CampaignResponse> 변환
        return campaignPage.map(Campaign::toCampaignResponse);
    }
}
