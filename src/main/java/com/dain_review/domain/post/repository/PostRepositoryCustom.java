package com.dain_review.domain.post.repository;

import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.CommunityType;
import com.dain_review.domain.post.model.entity.enums.FollowType;
import com.dain_review.domain.user.model.entity.enums.Role;

public interface PostRepositoryCustom {
    Long findPreviousPost(Long postId, Role role, CategoryType categoryType, CommunityType communityType, FollowType followType, String keyword);

    Long findNextPost(Long postId, Role role, CategoryType categoryType, CommunityType communityType, FollowType followType, String keyword);
}
