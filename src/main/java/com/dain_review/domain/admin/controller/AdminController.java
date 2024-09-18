package com.dain_review.domain.admin.controller;


import com.dain_review.domain.admin.model.request.CampaignApprovalRequest;
import com.dain_review.domain.admin.model.response.CampaignApprovalResponse;
import com.dain_review.domain.admin.service.AdminService;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/")
    public ResponseEntity getCampaigns(
            @RequestParam(value = "page", defaultValue = "0") String page,
            @RequestParam(value = "size", defaultValue = "10") String size,
            @RequestParam(value = "keyword", required = false) String keyword) {
        return adminService.getListCampaign(
                Integer.parseInt(page), Integer.parseInt(size), keyword);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')") // 관리자만 접근 가능하도록 설정
    @PatchMapping("/campaigns/{campaignId}/approve")
    public ResponseEntity<CampaignApprovalResponse> approveCampaign(
            @PathVariable Long campaignId, @RequestBody CampaignApprovalRequest request) {
        CampaignApprovalResponse response = adminService.approveCampaign(campaignId, request);
        return API.OK(response);
    }
}
