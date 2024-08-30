package com.dain_review.domain.campaign.model.request;


import com.dain_review.domain.campaign.model.entity.enums.Category;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import com.dain_review.domain.campaign.model.entity.enums.SortBy;
import com.dain_review.domain.campaign.model.entity.enums.State;
import com.dain_review.domain.campaign.model.entity.enums.Type;
import java.util.List;

public record CampaignSearchRequest(
        List<String> cities, // 시/도
        List<String> districts, // 구/군
        Category category, // 카테고리
        Platform platform, // 플랫폼
        Type type, // 체험단 유형
        State state, // 상태 (모집중, 검수중 등)
        String keyword, // 검색 키워드
        SortBy sortBy // 정렬 방식: 추천순, 인기순, 마감임박순, 최신순
        ) {}
