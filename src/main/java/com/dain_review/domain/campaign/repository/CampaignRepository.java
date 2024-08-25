package com.dain_review.domain.campaign.repository;


import com.dain_review.domain.campaign.model.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {}
