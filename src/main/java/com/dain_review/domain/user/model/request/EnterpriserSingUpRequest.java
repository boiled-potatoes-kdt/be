package com.dain_review.domain.user.model.request;

import com.dain_review.domain.user.model.entity.enums.SnsType;

import java.util.List;

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
        String impId
) {
}
