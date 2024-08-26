package com.dain_review.domain.user.model.response;


import java.util.List;

public record InfluencerResponse(
        String nickname,
        String profileImage,
        List<SnsResponse> snsResponseList,
        Integer likeCount,
        Long appliedCampaignCount, // 신청한 캠페인 수
        Long selectedCampaignCount, // 선정된 캠페인 수
        Long ongoingCampaignCount, // 진행중인 캠페인 수
        Long cancelledCampaignCount // 취소한 캠페인 수
        ) {}
