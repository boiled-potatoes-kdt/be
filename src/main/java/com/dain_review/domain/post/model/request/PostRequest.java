package com.dain_review.domain.post.model.request;

import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.CommunityType;
import com.dain_review.domain.post.model.entity.enums.FollowType;

import java.util.List;

public record PostRequest(
        String title,
        String content,
        CategoryType categoryType,
        CommunityType communityType,
        FollowType followType,
        List<String> deletedAttachedFiles

) { }
