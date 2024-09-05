package com.dain_review.domain.campaign.model.response;


import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CampaignSummaryResponse {
    /*체험단 목록 조회 시 반환할 데이터*/
    private Long id; // 체험단 ID
    private String businessName; // 상호명
    private String imageUrl; // 이미지 URL
    private Integer currentApplicants; // 현재 신청 인원
    private Integer capacity; // 최대 신청 인원
    private String status; // 현재 상태 (예: 모집중, 검수중 등)
    private Integer totalPoints; // 총 포인트
    private String city; // 시/도
    private String district; // 구/군
    private String type; // 체험단 유형
    private String label; // 라벨
    private String platform; // 플랫폼
    private LocalDateTime experienceStartDate; // 체험 시작일
    private LocalDateTime experienceEndDate; // 체험 종료일
    private Long applicationDeadline; // 지원 마감까지 남은 일수
    private Boolean isCancel; // 취소 가능한지 여부

    public static CampaignSummaryResponse fromEntity(Campaign campaign, String imageUrl) {
        // 지원 마감까지 남은 일수 계산
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, campaign.getApplicationEndDate());
        Long applicationDeadline = Math.max(duration.toDays(), 0);

        // 취소 가능 여부 판단 - 모집중, 모집완료, 체험&리뷰 단계에서만 가능
        Boolean isCancel =
                campaign.getCampaignState() == CampaignState.RECRUITING
                        || campaign.getCampaignState() == CampaignState.RECRUITMENT_COMPLETED
                        || campaign.getCampaignState() == CampaignState.EXPERIENCE_AND_REVIEW;

        return CampaignSummaryResponse.builder()
                .id(campaign.getId())
                .businessName(campaign.getBusinessName())
                .imageUrl(imageUrl)
                .currentApplicants(campaign.getCurrentApplicants())
                .capacity(campaign.getCapacity())
                .status(campaign.getCampaignState().getDisplayName())
                .totalPoints(campaign.getTotalPoints()) // 총 포인트가 있다면 포함, 없다면 null
                .city(campaign.getCity()) // 시/도
                .district(campaign.getDistrict()) // 구/군
                .type(campaign.getType().getDisplayName()) // 체험단 유형
                .label(campaign.getLabel().getDisplayName()) // 라벨
                .platform(campaign.getPlatform().getDisplayName()) // 플랫폼
                .experienceStartDate(campaign.getExperienceStartDate()) // 체험 시작일
                .experienceEndDate(campaign.getExperienceEndDate()) // 체험 종료일
                .applicationDeadline(applicationDeadline) // 지원 마감까지 남은 일수
                .isCancel(isCancel) // 취소 가능 여부
                .build();
    }
}
