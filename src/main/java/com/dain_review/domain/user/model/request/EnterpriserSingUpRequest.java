package com.dain_review.domain.user.model.request;

public record EnterpriserSingUpRequest(
        String email,
        String password,
        String name,
        String nickname,
        String joinPath,
        String company,
        Boolean terms,
        Boolean personalInformation,
        Boolean marketing,
        String impId) {}
