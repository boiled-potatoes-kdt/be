package com.dain_review.domain.user.controller;


import com.dain_review.domain.user.model.request.InfluencerOAuthSignUpRequest;
import com.dain_review.domain.user.model.request.InfluencerSignUpRequest;
import com.dain_review.domain.user.service.InfluencerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/influencer/sign-up")
public class InfluencerRegisterController {

    private final InfluencerService influencerService;

    @PostMapping
    public ResponseEntity signUpInfluencer(@RequestBody InfluencerSignUpRequest request) {
        return influencerService.signUpInfluencer(request);
    }

    @PostMapping("/oauth")
    public ResponseEntity signUpOAuthInfluencer(@RequestBody InfluencerOAuthSignUpRequest request) {
        return influencerService.signUpOAuthInfluencer(request);
    }
}
