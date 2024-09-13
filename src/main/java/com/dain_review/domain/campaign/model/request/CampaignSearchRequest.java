package com.dain_review.domain.campaign.model.request;


import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.entity.enums.Category;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import com.dain_review.domain.campaign.model.entity.enums.SortBy;
import com.dain_review.domain.campaign.model.entity.enums.Type;
import java.util.List;

public record CampaignSearchRequest(
        Long userId, // 유저 아이디
        List<String> cities, // 시/도
        List<String> districts, // 구/군
        Category category, // 카테고리
        Platform platform, // 플랫폼
        Type type, // 체험단 유형
        CampaignState campaignState, // 상태 (모집중, 검수중 등)
        boolean likedFilter, // 좋아요 목록 필터
        String keyword, // 검색 키워드
        SortBy sortBy // 정렬 방식: 추천순, 인기순, 마감임박순, 최신순
        ) {

    public boolean isLikedFilter() {
        return likedFilter;
    }

    // 유저 ID 가져오기
    public Long getUserId() {
        return userId;
    }
}
