package com.dain_review.domain.user.controller;


import com.dain_review.domain.user.model.request.EnterpriserOAuthSingUpRequest;
import com.dain_review.domain.user.model.request.EnterpriserSingUpRequest;
import com.dain_review.domain.user.service.EnterpriserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enterpriser/sing-up")
public class EnterpriserRegisterController {

    private final EnterpriserService enterpriserService;

    @PostMapping
    public ResponseEntity singUpEnterpriser(@RequestBody EnterpriserSingUpRequest request) {
        return enterpriserService.singUpEnterpriser(request);
    }

    @PostMapping("/oauth")
    public ResponseEntity singUpOAuthEnterpriser(
            @RequestBody EnterpriserOAuthSingUpRequest request) {
        return enterpriserService.singUpOAuthEnterpriser(request);
    }
}
