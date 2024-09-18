package com.dain_review.domain.user.controller;


import com.dain_review.domain.user.model.request.FindUserIdRequest;
import com.dain_review.domain.user.model.request.PasswordChangeRequest;
import com.dain_review.domain.user.model.request.PasswordCheckRequest;
import com.dain_review.domain.user.service.FindUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FindUserController {

    private final FindUserService findUserService;

    @PostMapping("/id/find")
    public ResponseEntity findUserId(@RequestBody FindUserIdRequest request) {
        return findUserService.findUserId(request);
    }

    @PostMapping("/password/change")
    public ResponseEntity passwordChange(@RequestBody PasswordChangeRequest request) {
        return findUserService.passwordChange(request);
    }

    @PostMapping("/password/change/check")
    public ResponseEntity passwordCheck(@RequestBody PasswordCheckRequest request) {
        return findUserService.passwordCheck(request);
    }
}
