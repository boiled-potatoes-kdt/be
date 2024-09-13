package com.dain_review.domain.user.model.request;


import com.dain_review.domain.user.model.entity.enums.OAuthType;
import java.util.List;

public record InfluencerOAuthSignUpRequest(
        String email,
        String name,
        String nickname,
        List<SingUpSns> snsResponseList,
        String joinPath,
        Boolean terms,
        Boolean personalInformation,
        Boolean marketing,
        String impId,
        OAuthType type) {}
