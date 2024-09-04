package com.dain_review.domain.auth.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoUserInfo {
    private final String name;
    private final String email;
    private final String profileImage;
    public KakaoUserInfo(Map<String, Object> attributes) {
        this.name = getNicknameByAttributes(attributes);
        this.email = getEmailByAttributes(attributes);
        this.profileImage = (String) attributes.get("profile_image");
    }

    public String getEmailByAttributes(Map<String, Object> attributes) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeReferencer = new TypeReference<>() {
        };

        Object kakaoAccount = attributes.get("kakao_account");
        Map<String, Object> account = objectMapper.convertValue(kakaoAccount, typeReferencer);

        return (String) account.get("email");
    }

    public String getNicknameByAttributes(Map<String, Object> attributes) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeReferencer = new TypeReference<>() {
        };

        Object kakaoAccount = attributes.get("kakao_account");
        Map<String, Object> account = objectMapper.convertValue(kakaoAccount, typeReferencer);
        Map<String, Object> profile = objectMapper.convertValue(account.get("profile"), typeReferencer);

        return (String) profile.get("nickname");
    }
}