package com.dain_review.domain.auth.service;


import com.dain_review.domain.auth.client.GoogleApiClient;
import com.dain_review.domain.auth.client.KakaoApiClient;
import com.dain_review.domain.auth.client.NaverApiClient;
import com.dain_review.domain.auth.model.GoogleUserInfo;
import com.dain_review.domain.auth.model.KakaoUserInfo;
import com.dain_review.domain.auth.model.NaverUserInfo;
import com.dain_review.domain.auth.model.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoApiClient kakaoApiClient;
    private final GoogleApiClient googleApiClient;
    private final NaverApiClient naverApiClient;

    public void getKakaoUserInfo(String code) {
        TokenResponse token = kakaoApiClient.getKakaoToken(code);
        KakaoUserInfo info = kakaoApiClient.getKakaoUserInfo(token.getAccessToken());
    }

    public void getGoogleUserInfo(String code) {
        TokenResponse token = googleApiClient.getGoogleToken(code);
        GoogleUserInfo info = googleApiClient.getGoogleUserInfo(token.getAccessToken());
    }

    public void getNaverUserInfo(String code) {
        TokenResponse token =naverApiClient.getNaverToken(code);
        NaverUserInfo info = naverApiClient.getNaverUserInfo(token.getAccessToken());
    }

}
