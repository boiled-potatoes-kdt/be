package com.dain_review.domain.user.controller;


import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.domain.user.model.request.ProfileChangeRequest;
import com.dain_review.domain.user.service.UserService;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 프로필 사진 수정
    @PatchMapping("/profile")
    public ResponseEntity update(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ProfileChangeRequest profileChangeRequest) {

        Long userId = customUserDetails.getUserId();
        userService.update(userId, profileChangeRequest);

        return API.OK();
    }

    // Todo - 응답형식 어떻게 해야하는지 물어보기
    // 회원 탈퇴
    @DeleteMapping("")
    public ResponseEntity delete(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long userId = customUserDetails.getUserId();
        userService.delete(userId);

        return API.OK();
    }
}
