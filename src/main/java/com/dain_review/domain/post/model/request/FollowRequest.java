package com.dain_review.domain.post.model.request;

import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.FollowType;

import java.util.List;

public record FollowRequest(
        String title,
        String content,
        CategoryType categoryType,
        FollowType followType,
        List<String> deleted
) { }
