package com.dain_review.domain.campaign.model.response;


import com.dain_review.domain.campaign.model.entity.enums.Label;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import com.dain_review.domain.campaign.model.entity.enums.State;
import com.dain_review.domain.campaign.model.entity.enums.Type;
import java.time.LocalDateTime;

public record ApplicationCampaignResponse(
        /*신청자의 관점의 캠페인 정보*/
        Long id,
        String nickname,
        String serviceProvided,
        String city,
        String district,
        Type type,
        Platform platform,
        String imageUrl,
        State state,
        Integer capacity,
        Integer applicant,
        LocalDateTime experienceStartDate,
        LocalDateTime experienceEndDate,
        Long applicationDeadline, // 지원 마감까지 남은 일수
        Boolean isCancel, // 취소 가능한지 여부
        Label label,
        Boolean isLike // 찜 했는지 여부
        ) {}
