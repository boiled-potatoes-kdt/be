package com.dain_review.domain.application.controller;


import com.dain_review.domain.application.ApplicationBusiness;
import com.dain_review.domain.application.model.request.ApplicationRequest;
import com.dain_review.domain.application.service.ApplicationService;
import com.dain_review.domain.campaign.model.request.CampaignFilterRequest;
import com.dain_review.domain.campaign.model.response.ApplicationCampaignResponse;
import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/influencer/application")
public class ApplicationController {

    private final ApplicationBusiness applicationBusiness;
    private final ApplicationService applicationService;

    // 체험단 신청
    @PostMapping
    public ResponseEntity<?> applyCampaign(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ApplicationRequest applicationRequest) {

        applicationBusiness.save(applicationRequest, customUserDetails.getUserId());
        return ResponseEntity.ok(API.OK());
    }

    // 체험단 신청 취소
    @PatchMapping("/{applicationId}")
    public ResponseEntity<?> cancelApplication(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long applicationId) {

        applicationService.cancelApplication(applicationId, customUserDetails.getUserId());
        return ResponseEntity.ok(API.OK());
    }

    // 인플루언서 마이페이지에서 신청한 체험단 리스트 페이지네이션으로 가져오기
    @GetMapping
    public ResponseEntity<Page<ApplicationCampaignResponse>> getApplications(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @ModelAttribute CampaignFilterRequest campaignFilterRequest,
            Pageable pageable) {

        Page<ApplicationCampaignResponse> response =
                applicationService.getApplications(
                        campaignFilterRequest, pageable, customUserDetails.getUserId());

        return ResponseEntity.ok(response);
    }
}
