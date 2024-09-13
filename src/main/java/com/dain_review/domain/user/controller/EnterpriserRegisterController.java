package com.dain_review.domain.user.controller;


import com.dain_review.domain.user.model.request.EnterpriserOAuthSignUpRequest;
import com.dain_review.domain.user.model.request.EnterpriserSignUpRequest;
import com.dain_review.domain.user.service.EnterpriserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enterpriser/sign-up")
public class EnterpriserRegisterController {

    private final EnterpriserService enterpriserService;

    @PostMapping
    public ResponseEntity singUpEnterpriser(@RequestBody EnterpriserSignUpRequest request) {
        return enterpriserService.signUpEnterpriser(request);
    }

    @PostMapping("/oauth")
    public ResponseEntity singUpOAuthEnterpriser(
            @RequestBody EnterpriserOAuthSignUpRequest request) {
        return enterpriserService.singUpOAuthEnterpriser(request);
    }
}
