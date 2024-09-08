package com.dain_review.domain.post.model.request;

import com.dain_review.domain.post.model.entity.enums.CommunityType;
import com.dain_review.domain.post.model.entity.enums.FollowType;

public record PostSearchRequest(
        CommunityType communityType,
        FollowType followType,
        String keyword
) {
}
