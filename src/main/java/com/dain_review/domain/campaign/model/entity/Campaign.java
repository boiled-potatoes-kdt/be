package com.dain_review.domain.campaign.model.entity;


import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.entity.enums.Category;
import com.dain_review.domain.campaign.model.entity.enums.Label;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import com.dain_review.domain.campaign.model.entity.enums.Type;
import com.dain_review.domain.campaign.util.CampaignUtil;
import com.dain_review.domain.choice.model.entity.Choice;
import com.dain_review.domain.review.model.entity.Review;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(
        name = "campaign",
        indexes = {@Index(name = "idx_is_deleted", columnList = "isDeleted")})
public class Campaign extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 체험단을 등록한 사용자

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    private List<Application> applicationList;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    private List<Choice> choiceList;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    private List<Review> ReviewList;

    @Enumerated(EnumType.STRING)
    private Platform platform; // 광고를 원하는 플랫폼 (예: 블로그, 인스타그램)

    @Enumerated(EnumType.STRING)
    private Type type; // 광고를 원하는 유형 (예: 방문형, 구매형)

    @Enumerated(EnumType.STRING)
    private Category category; // 광고를 원하는 카테고리 (예: 음식, 뷰티)

    private String serviceProvided; // 제공 내역

    private String businessName; // 상호명

    private String imageUrl; // 이미지 등록 URL

    private String contactNumber; // 연락처

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_ordering_id")
    private LabelOrdering labelOrdering;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "available_days", joinColumns = @JoinColumn(name = "campaign_id"))
    @Column(name = "day")
    private Set<String> availableDays; // 체험 가능 요일 (월, 화, 수, 목, 금, 토, 일)

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "keywords", joinColumns = @JoinColumn(name = "campaign_id"))
    @Column(name = "keyword")
    private Set<String> keywords; // 홍보용 키워드(태그) 최대 3개, 각 키워드는 10자 이내

    private Boolean pointPayment; // 포인트 지급 여부 (예/아니오)

    private Integer capacity; // 총 모집 인원 수

    private Integer currentApplicants; // 현재까지 신청 인원 수

    private Integer pointPerPerson; // 1인당 지급 포인트

    private Integer totalPoints; // 총 지급 포인트 (총 모집 인원 * 1인당 지급 포인트 * 수수료 20%)

    private String address; // 방문 체험할 장소의 주소

    private Integer postalCode; // 우편번호

    private Double latitude; // 위도

    private Double longitude; // 경도

    private String city; // 시/도

    private String district; // 구/군

    @Enumerated(EnumType.STRING)
    private CampaignState campaignState; // 체험단 상태 (검수중, 모집중 등)

    @Enumerated(EnumType.STRING)
    private Label label; // 라벨 (예: 다인체험단, 프리미엄, 일반체험단)

    private String requirement; // 사업주 요청 사항

    private LocalDateTime applicationStartDate; // 모집 시작일

    private LocalDateTime applicationEndDate; // 모집 종료일

    private LocalDateTime announcementDate; // 선정자 발표일

    private LocalDateTime experienceStartDate; // 체험 시작일

    private LocalDateTime experienceEndDate; // 체험 종료일

    private LocalDateTime reviewDate; // 리뷰 마감일

    private Boolean isDeleted; // 삭제 여부

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setAddress(String address) {
        this.address = address;
        String[] cityAndDistrict = CampaignUtil.extractCityAndDistrict(address);
        this.city = cityAndDistrict[0];
        this.district = cityAndDistrict[1];
    }

    public void calculateAndSetTotalPoints() {
        this.totalPoints = CampaignUtil.calculateTotalPoints(this.capacity, this.pointPerPerson);
    }
}
