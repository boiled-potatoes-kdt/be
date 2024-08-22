package com.dain_review.domain.campaign.controller;

import com.dain_review.domain.campaign.model.response.CampaignHomeResponse;
import com.dain_review.domain.campaign.service.CampaignService;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping("/home")
    public ResponseEntity getCampaignForEachCategory() {
        CampaignHomeResponse campaignHomeResponse = campaignService.getCampaignForHomeScreen();
        return API.OK(campaignHomeResponse);
    }
}
