package com.dain_review.domain.campaign.model.request;


import com.dain_review.domain.campaign.model.entity.enums.Category;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import com.dain_review.domain.campaign.model.entity.enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

public record CampaignRequest( // 체험단 등록 시 사용할 Request DTO
        @NotBlank String businessName, // 상호명
        @NotBlank String contactNumber, // 컨택할 번호
        @NotBlank String address,
        @NotBlank Integer postalCode,
        @NotBlank Double latitude, // 위도
        @NotBlank Double longitude, // 경도
        @NotNull Set<String> availableDays, // 체험 가능 요일
        @NotNull Type type, // 체험단 유형 (예: 방문형, 구매형)
        @NotNull Category category, // 카테고리 (예: 음식, 뷰티)
        @NotNull Platform platform, // 플랫폼 (예: 블로그, 인스타그램)
        @NotNull Integer capacity, // 최대 신청 인원
        @NotBlank String serviceProvided, // 제공 내역
        @NotBlank String requirement, // 사업주 요청사항
        @Size(max = 3) Set<@Size(max = 10) String> keywords, // 홍보용 키워드 최대 3개, 각 키워드는 10자 이내
        @NotNull Boolean pointPayment, // 포인트 지급 여부(예/아니오)
        Integer pointPerPerson, // 1인당 지급 포인트
        @NotNull LocalDateTime applicationStartDate, // 모집 시작일
        @NotNull LocalDateTime applicationEndDate, // 모집 종료일
        @NotNull LocalDateTime announcementDate, // 선정자 발표일
        @NotNull LocalDateTime experienceStartDate, // 체험 시작일
        @NotNull LocalDateTime experienceEndDate, // 체험 종료일
        @NotNull LocalDateTime reviewDate // 리뷰 마감일
        ) {}
