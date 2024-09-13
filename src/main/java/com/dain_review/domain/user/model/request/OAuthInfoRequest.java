package com.dain_review.domain.user.model.request;

import com.dain_review.domain.user.model.entity.enums.OAuthType;
import com.dain_review.domain.user.model.entity.enums.SnsType;

public record OAuthInfoRequest(
        String code,
        OAuthType type
) {
}
