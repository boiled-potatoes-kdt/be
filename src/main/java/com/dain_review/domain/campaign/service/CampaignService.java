package com.dain_review.domain.campaign.service;

import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.Label;
import com.dain_review.domain.campaign.model.response.CampaignHomeResponse;
import com.dain_review.domain.campaign.model.response.CampaignPreviewResponse;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;

    // 홈 화면에 들어갈 데이터
    public CampaignHomeResponse getCampaignForHomeScreen() {
        List<Campaign> premium = campaignRepository.findByLabel(Label.PREMIUM, null, null, null, PageRequest.of(0, 8)).getContent();
        List<CampaignPreviewResponse> premiumPreview = premium.stream()
                .map(CampaignPreviewResponse::from).toList();
        List<Campaign> popular = campaignRepository.orderByApplicant(null, null, null, PageRequest.of(0, 8)).getContent();
        List<CampaignPreviewResponse> popularPreview = popular.stream()
                .map(CampaignPreviewResponse::from).toList();
        List<Campaign> newest = campaignRepository.orderByApprovalDate(null, null, null, PageRequest.of(0, 3)).getContent();
        List<CampaignPreviewResponse> newestPreview = newest.stream()
                .map(CampaignPreviewResponse::from).toList();
        List<Campaign> imminent = campaignRepository.orderByImminentDueDate(null, null, null, PageRequest.of(0, 3)).getContent();
        List<CampaignPreviewResponse> imminentPreview = imminent.stream()
                .map(CampaignPreviewResponse::from).toList();

        return CampaignHomeResponse.builder()
                .premium(premiumPreview)
                .popular(popularPreview)
                .newest(newestPreview)
                .imminent(imminentPreview)
                .build();
    }

}
