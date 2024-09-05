package com.dain_review.domain.campaign.repository;


import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.request.CampaignSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CampaignRepositoryCustom {
    Page<Campaign> searchCampaigns(CampaignSearchRequest searchRequest, Pageable pageable);
}
