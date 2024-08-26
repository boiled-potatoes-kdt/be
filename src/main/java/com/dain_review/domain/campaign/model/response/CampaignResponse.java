package com.dain_review.domain.campaign.model.response;


import com.dain_review.domain.campaign.model.entity.Campaign;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CampaignResponse {
    private Long id;
    private String businessName; // 상호명
    private String imageUrl; // 이미지 등록 URL
    private String contactNumber; // 연락처
    private String address; // 방문 체험할 장소의 주소
    private Double latitude; // 위도
    private Double longitude; // 경도
    private List<String> availableDays; // 체험 가능 요일 (월, 화, 수, 목, 금, 토, 일)
    private String type; // 체험단 유형
    private String category; // 카테고리
    private String platform; // 플랫폼
    private String label;
    private Integer capacity; // 최대 신청 인원
    private String serviceProvided; // 제공 내역
    private String requirement;
    private List<String> keywords; // 홍보용 키워드
    private Boolean pointPayment; // 포인트 지급 여부
    private Integer pointPerPerson; // 1인당 지급 포인트
    private Integer totalPoints; // 총 지급 포인트
    private LocalDateTime applicationStartDate; // 모집 시작일
    private LocalDateTime applicationEndDate; // 모집 종료일
    private LocalDateTime announcementDate; // 선정자 발표일
    private LocalDateTime experienceStartDate; // 체험 시작일
    private LocalDateTime experienceEndDate; // 체험 종료일
    private LocalDateTime reviewDate; // 리뷰 마감일

    public static CampaignResponse fromEntity(Campaign campaign, String imageUrl) {
        return CampaignResponse.builder()
                .id(campaign.getId())
                .businessName(campaign.getBusinessName())
                .imageUrl(imageUrl)
                .contactNumber(campaign.getContactNumber())
                .type(campaign.getType().getDisplayName())
                .category(campaign.getCategory().getDisplayName())
                .platform(campaign.getPlatform().getDisplayName())
                .label(campaign.getLabel() != null ? campaign.getLabel().getDisplayName() : null)
                .capacity(campaign.getCapacity())
                .serviceProvided(campaign.getServiceProvided())
                .requirement(campaign.getRequirement())
                .keywords(campaign.getKeywords())
                .pointPayment(campaign.getPointPayment())
                .pointPerPerson(campaign.getPointPerPerson())
                .totalPoints(campaign.getTotalPoints())
                .address(campaign.getAddress()) // 주소
                .latitude(campaign.getLatitude()) // 위도
                .longitude(campaign.getLongitude()) // 경도
                .availableDays(campaign.getAvailableDays()) // 체험 가능 요일
                .applicationStartDate(campaign.getApplicationStartDate())
                .applicationEndDate(campaign.getApplicationEndDate())
                .announcementDate(campaign.getAnnouncementDate())
                .experienceStartDate(campaign.getExperienceStartDate())
                .experienceEndDate(campaign.getExperienceEndDate())
                .reviewDate(campaign.getReviewDate())
                .build();
    }
}
