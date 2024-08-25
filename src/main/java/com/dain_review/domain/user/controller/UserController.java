package com.dain_review.domain.user.controller;


import com.dain_review.domain.user.model.request.ProfileChangeRequest;
import com.dain_review.domain.user.service.UserService;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity update(@RequestBody ProfileChangeRequest profileChangeRequest) {

        Long id = 1L;

        userService.update(id, profileChangeRequest);

        return API.OK();
    }

    // 회원 탈퇴

}
