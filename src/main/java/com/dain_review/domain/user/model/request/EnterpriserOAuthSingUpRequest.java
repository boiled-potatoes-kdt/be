package com.dain_review.domain.user.model.request;


import com.dain_review.domain.user.model.entity.enums.OAuthType;

public record EnterpriserOAuthSingUpRequest(
        String email,
        String name,
        String nickname,
        String joinPath,
        String company,
        Boolean terms,
        Boolean personalInformation,
        Boolean marketing,
        String impId,
        OAuthType type) {}
