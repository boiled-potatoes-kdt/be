package com.dain_review.domain.review.controller;

import com.dain_review.domain.review.model.request.ReviewRequest;
import com.dain_review.domain.review.service.ReviewService;
import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/application/{application_id}/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER')")
    @PostMapping
    public ResponseEntity<?> createReview(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("application_id") Long applicationId,
            @RequestPart("data") ReviewRequest reviewRequest,
            @RequestPart(value = "imageFile") List<MultipartFile> imageFiles
    ) {
        reviewService.createReview(customUserDetails.getUserId(), applicationId, reviewRequest, imageFiles);
        return API.OK("리뷰 등록이 완료되었습니다.");
    }
}
