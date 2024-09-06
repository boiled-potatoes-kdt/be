package com.dain_review.domain.campaign.model.response;


import com.dain_review.domain.campaign.model.entity.Campaign;
import java.time.Duration;
import java.time.LocalDateTime;

public record CampaignSummaryResponse(
        /*체험단 목록 조회 시 반환할 데이터*/
        Long id, // 체험단 ID
        String businessName, // 상호명
        String imageUrl, // 이미지 URL
        Integer currentApplicants, // 현재 신청 인원
        Integer capacity, // 최대 신청 인원
        String campaignState, // 체험단 상태 (예: 모집중, 검수중 등)
        Integer totalPoints, // 총 포인트
        String city, // 시/도
        String district, // 구/군
        String type, // 체험단 유형
        String label, // 라벨
        String platform, // 플랫폼
        LocalDateTime experienceStartDate, // 체험 시작일
        LocalDateTime experienceEndDate, // 체험 종료일
        Long applicationDeadline, // 지원 마감까지 남은 일수
        Boolean isCancel // 취소 가능한지 여부
        ) {
    public static CampaignSummaryResponse from(Campaign campaign, String imageUrl) {
        // 지원 마감까지 남은 일수 계산
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, campaign.getApplicationEndDate());
        Long applicationDeadline = Math.max(duration.toDays(), 0);

        // 엔티티에서 취소 가능 여부 판단
        Boolean isCancel = campaign.isCancelable();

        return new CampaignSummaryResponse(
                campaign.getId(),
                campaign.getBusinessName(),
                imageUrl,
                campaign.getCurrentApplicants(),
                campaign.getCapacity(),
                campaign.getCampaignState().name(), // CampaignState 자체를 반환
                campaign.getTotalPoints(),
                campaign.getCity(),
                campaign.getDistrict(),
                campaign.getType().getDisplayName(),
                campaign.getLabel().getDisplayName(),
                campaign.getPlatform().getDisplayName(),
                campaign.getExperienceStartDate(),
                campaign.getExperienceEndDate(),
                applicationDeadline,
                isCancel);
    }
}
