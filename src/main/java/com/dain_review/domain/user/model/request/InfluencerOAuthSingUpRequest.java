package com.dain_review.domain.user.model.request;


import com.dain_review.domain.user.model.entity.enums.OAuthType;
import com.dain_review.domain.user.model.entity.enums.SnsType;

import java.util.List;

public record InfluencerOAuthSingUpRequest(
        String email,
        String name,
        String nickname,
        List<SingUpSns> snsResponseList,
        String joinPath,
        Boolean terms,
        Boolean personalInformation,
        Boolean marketing,
        String impId,
        OAuthType type) {


}
