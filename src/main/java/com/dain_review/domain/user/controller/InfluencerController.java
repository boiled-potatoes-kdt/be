package com.dain_review.domain.user.controller;


import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.domain.user.model.request.InfluencerChangeRequest;
import com.dain_review.domain.user.model.request.InfluencerExtraRegisterRequest;
import com.dain_review.domain.user.model.response.InfluencerChangeResponse;
import com.dain_review.domain.user.model.response.InfluencerResponse;
import com.dain_review.domain.user.service.InfluencerService;
import com.dain_review.global.api.API;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/influencer")
@RequiredArgsConstructor
public class InfluencerController {

    private final InfluencerService influencerService;

    // 인플루언서 회원가입 추가정보 입력
    @PostMapping("/sign-up/extra")
    public ResponseEntity save(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody InfluencerExtraRegisterRequest influencerExtraRegisterRequest) {

        influencerService.signUpExtra(
                customUserDetails.getUserId(), influencerExtraRegisterRequest);

        return API.OK();
    }

    // 인플루언서 마이페이지
    @GetMapping("")
    public ResponseEntity<InfluencerResponse> get(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        InfluencerResponse influencerResponse =
                influencerService.getMyPage(customUserDetails.getUserId());

        return API.OK(influencerResponse);
    }

    // 인플루언서 회원정보 변경 페이지
    @GetMapping("/change")
    public ResponseEntity<InfluencerChangeResponse> change(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        InfluencerChangeResponse influencerChangeResponse =
                influencerService.getChangePage(customUserDetails.getUserId());

        return API.OK(influencerChangeResponse);
    }

    // 인플루언서 회원정보 변경
    @PatchMapping("")
    public ResponseEntity<InfluencerChangeResponse> change(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody InfluencerChangeRequest influencerChangeRequest) {

        InfluencerChangeResponse influencerChangeResponse =
                influencerService.changeInformation(
                        influencerChangeRequest, customUserDetails.getUserId());

        return API.OK(influencerChangeResponse);
    }
}
