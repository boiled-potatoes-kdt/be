package com.dain_review.domain.auth.controller;

import com.dain_review.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService kakaoService;

    @RequestMapping("/login/oauth2/code/kakao")
    public ResponseEntity loginKakao(
            @RequestParam(value = "code") String code
    ) {
        System.out.println("kakao request: " + code);
        kakaoService.getKakaoUserInfo(code);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/login/oauth2/code/naver")
    public ResponseEntity loginNaver(
            @RequestParam(value = "code", required = false) String code
    ) {
        System.out.println("naver request: " + code);
        kakaoService.getNaverUserInfo(code);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/login/oauth2/code/google")
    public ResponseEntity loginGoogle(
            @RequestParam(value = "code") String code
    ) {
        System.out.println("google request: " + code);
        kakaoService.getGoogleUserInfo(code);
        return ResponseEntity.ok().build();
    }

}
