package com.dain_review.domain.like.controller;


import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.like.service.LikeService;
import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.global.api.API;
import com.dain_review.global.model.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("{campaignId}")
    public ResponseEntity<?> toggleLike(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long campaignId) {
        boolean isLiked = likeService.toggleLike(customUserDetails.getUserId(), campaignId);
        return API.OK(isLiked);
    }

    @GetMapping
    public ResponseEntity<?> getLikedCampaigns(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10) Pageable pageable) {
        PagedResponse<CampaignResponse> likedCampaigns =
                likeService.getLikedCampaigns(customUserDetails.getUserId(), pageable);
        return API.OK(likedCampaigns);
    }
}
