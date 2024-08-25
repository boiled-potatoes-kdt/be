package com.dain_review.domain.campaign.model.entity;


import com.dain_review.domain.campaign.model.entity.enums.Category;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import com.dain_review.domain.campaign.model.entity.enums.State;
import com.dain_review.domain.campaign.model.entity.enums.Type;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Campaign extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 체험단을 등록한 사용자

    @Enumerated(EnumType.STRING)
    private Platform platform; // 광고를 원하는 플랫폼 (예: 블로그, 인스타그램)

    @Enumerated(EnumType.STRING)
    private Type type; // 광고를 원하는 유형 (예: 방문형, 구매형)

    @Enumerated(EnumType.STRING)
    private Category category; // 광고를 원하는 카테고리 (예: 음식, 뷰티)

    private String serviceProvided; // 인플루언서에게 제공할 서비스

    private String businessName; // 상호명

    private String imageUrl; // 이미지 등록 URL

    private String address; // 방문 체험할 장소의 주소

    private String contactNumber; // 연락처

    @ElementCollection
    @CollectionTable(name = "available_days", joinColumns = @JoinColumn(name = "campaign_id"))
    @Column(name = "day")
    private List<String> availableDays; // 체험 가능 요일 (월, 화, 수, 목, 금, 토, 일)

    private LocalTime startTime; // 체험 가능 시작 시간 (HH:MM)

    private LocalTime endTime; // 체험 가능 종료 시간 (HH:MM)

    private String mission; // 사업주 미션

    @ElementCollection
    @CollectionTable(name = "keywords", joinColumns = @JoinColumn(name = "campaign_id"))
    @Column(name = "keyword")
    private List<String> keywords; // 홍보용 키워드 최대 3개, 각 키워드는 10자 이내

    private Boolean pointPayment; // 포인트 지급 여부 (예/아니오)

    private Integer capacity; // 총 모집 인원 수

    private Integer pointPerPerson; // 1인당 지급 포인트

    private Integer totalPoints; // 총 지급 포인트 (총 모집 인원 * 1인당 지급 포인트 * 수수료 20%)

    private Double latitude; // 위도

    private Double longitude; // 경도

    private String region1; // 지역 1 (시/도)

    private String region2; // 지역 2 (구/군)

    @Enumerated(EnumType.STRING)
    private State state; // 체험단 상태 (검수증, 모집중 등)

    private String label; // 라벨 (예: 다인체험단,프리미엄,일반)

    private String reward; // 제공 내역

    private String notation; // 주의 사항

    private String information; // 체험단 정보

    private String requirement; // 요구 사항

    private Boolean today; // 오늘 체험 여부

    private LocalDateTime applicationStartDate; // 모집 시작일

    private LocalDateTime applicationEndDate; // 모집 종료일

    private LocalDateTime announcementDate; // 발표일

    private LocalDateTime experienceStartDate; // 체험 시작일

    private LocalDateTime experienceEndDate; // 체험 종료일

    private LocalDateTime reviewDate; // 리뷰 마감일

    private Boolean isDeleted; // 삭제 여부

    public void markAsDeleted() {
        this.isDeleted = true;
    }
}
