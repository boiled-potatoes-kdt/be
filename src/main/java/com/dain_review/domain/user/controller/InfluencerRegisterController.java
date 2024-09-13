package com.dain_review.domain.user.controller;


import com.dain_review.domain.user.model.request.InfluencerOAuthSingUpRequest;
import com.dain_review.domain.user.model.request.InfluencerSingUpRequest;
import com.dain_review.domain.user.service.InfluencerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/influencer/sing-up")
public class InfluencerRegisterController {

    private final InfluencerService influencerService;

    @PostMapping
    public ResponseEntity singUpInfluencer(@RequestBody InfluencerSingUpRequest request) {
        return influencerService.singUpInfluencer(request);
    }

    @PostMapping("/oauth")
    public ResponseEntity singUpOAuthInfluencer(@RequestBody InfluencerOAuthSingUpRequest request) {
        return influencerService.singUpOAuthInfluencer(request);
    }
}

