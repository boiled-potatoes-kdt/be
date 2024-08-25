package com.dain_review.domain.campaign.controller;


import com.dain_review.domain.campaign.model.request.CampaignRequest;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.campaign.service.CampaignService;
import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    @PreAuthorize("hasAnyRole('ROLE_ENTERPRISER')")
    @PostMapping // 체험단 생성
    public ResponseEntity<?> createCampaign(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart("data") CampaignRequest campaignRequest,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        CampaignResponse campaignResponse =
                campaignService.createCampaign(
                        customUserDetails.getUserId(), campaignRequest, imageFile);
        return API.OK(campaignResponse);
    }

    /*	@PreAuthorize("hasAnyRole('ROLE_ENTERPRISER')")
    @PatchMapping("/{campaignId}") // 체험단 수정
    public ResponseEntity<?> updateCampaign(
    	@AuthenticationPrincipal CustomUserDetails customUserDetails,
    	@PathVariable Long campaignId,
    	@RequestPart("data") CampaignRequest campaignRequest,
    	@RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

    	CampaignResponse campaignResponse =
    		campaignService.updateCampaign(customUserDetails.getUserId(), campaignId, campaignRequest, imageFile);
    	return API.OK(campaignResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_ENTERPRISER')")
    @DeleteMapping("/{campaignId}") // 체험단 삭제
    public ResponseEntity<?> deleteCampaign(
    	@AuthenticationPrincipal CustomUserDetails customUserDetails,
    	@PathVariable Long campaignId) {

    	campaignService.deleteCampaign(customUserDetails.getUserId(), campaignId);
    	return API.OK();
    }

    @PreAuthorize("hasAnyRole('ROLE_ENTERPRISER')")
    @GetMapping("/{campaignId}") // 체험단 단건 조회
    public ResponseEntity<?> getCampaign(
    	@AuthenticationPrincipal CustomUserDetails customUserDetails,
    	@PathVariable Long campaignId) {

    	CampaignResponse campaignResponse =
    		campaignService.getCampaign(customUserDetails.getUserId(), campaignId);
    	return API.OK(campaignResponse);
    }*/
}
