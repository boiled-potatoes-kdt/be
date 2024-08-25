package com.dain_review.domain.campaign.model.response;


import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.entity.enums.Label;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import com.dain_review.domain.campaign.model.entity.enums.Type;
import java.time.LocalDateTime;

public record CampaignResponse(
        Long id,
        String nickname,
        String reward,
        String region1,
        String region2,
        Type type,
        Platform platform,
        String profileImage,
        CampaignState campaignState,
        Integer capacity,
        Integer applicant,
        LocalDateTime experienceStartDate,
        LocalDateTime experienceEndDate,
        Long applicationDeadline, // 지원 마감까지 남은 일수
        Boolean isCancel, // 취소가능한지
        Label label,
        Boolean isLike // 찜 했는지
        ) {}
