package com.dain_review.domain.user.model.request;


import com.dain_review.domain.user.model.entity.enums.SnsType;
import java.util.List;

public record InfluencerSingUpRequest(
        String email,
        String password,
        String name,
        String nickname,
        List<SnsType> sns,
        String joinPath,
        Boolean terms,
        Boolean personalInformation,
        Boolean marketing,
        String impId) {}
