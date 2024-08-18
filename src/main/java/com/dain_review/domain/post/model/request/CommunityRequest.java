package com.dain_review.domain.post.model.request;


import com.dain_review.domain.post.model.type.CategoryType;
import com.dain_review.domain.post.model.type.CommunityType;

public record CommunityRequest(
        String title, String content, CommunityType communityType, CategoryType categoryType) {}
