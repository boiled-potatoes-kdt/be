package com.dain_review.domain.user.controller;

import com.dain_review.domain.user.model.request.EmailCheckRequest;
import com.dain_review.domain.user.service.SingUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sing-up")
public class SingUpController {

    private final SingUpService service;

    @PostMapping("/email/check")
    public ResponseEntity emailCheck(
            @RequestBody EmailCheckRequest request
    ) {
        return service.emailCheck(request);
    }

}
