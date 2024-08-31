package com.dain_review.domain.campaign.controller;


import com.dain_review.domain.campaign.model.request.CampaignFilterRequest;
import com.dain_review.domain.campaign.model.request.CampaignRequest;
import com.dain_review.domain.campaign.model.request.CampaignSearchRequest;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.campaign.model.response.CampaignSummaryResponse;
import com.dain_review.domain.campaign.service.CampaignService;
import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.global.api.API;
import com.dain_review.global.model.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
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

    @GetMapping("/{campaignId}") // 체험단 단건 조회
    public ResponseEntity<?> getCampaign(@PathVariable Long campaignId) {
        CampaignResponse campaignResponse = campaignService.getCampaignById(campaignId);
        return API.OK(campaignResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_ENTERPRISER')")
    @PatchMapping("/{campaignId}") // 체험단 삭제(취소)
    public ResponseEntity<?> deleteCampaign(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long campaignId) {

        campaignService.deleteCampaign(customUserDetails.getUserId(), campaignId);
        return API.OK();
    }

    @PreAuthorize("hasAnyRole('ROLE_ENTERPRISER')")
    @GetMapping
    public ResponseEntity<Page<CampaignSummaryResponse>>
            getRegisteredCampaigns( // 사업주가 등록한 체험단 목록 조회
                    @AuthenticationPrincipal CustomUserDetails customUserDetails,
                    @ModelAttribute CampaignFilterRequest campaignFilterRequest,
                    @PageableDefault(page = 1, size = 10) Pageable pageable) {

        Page<CampaignSummaryResponse> campaigns =
                campaignService.getRegisteredCampaigns(
                        campaignFilterRequest, pageable, customUserDetails.getUserId());

        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<CampaignSummaryResponse>> searchCampaigns( // 검색 필터로 체험단 검색
            CampaignSearchRequest searchRequest, @PageableDefault(size = 10) Pageable pageable) {

        PagedResponse<CampaignSummaryResponse> campaigns =
                campaignService.searchCampaigns(searchRequest, pageable);
        return ResponseEntity.ok(campaigns);
    }
}
