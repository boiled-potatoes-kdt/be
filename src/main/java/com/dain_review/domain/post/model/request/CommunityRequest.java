package com.dain_review.domain.post.model.request;


import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.CommunityType;

import java.util.List;

public record CommunityRequest(
        String title, String content, CommunityType communityType, CategoryType categoryType, List<String> deleted) {}
