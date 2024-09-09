package com.dain_review.domain.campaign.model.entity;


import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.entity.enums.Category;
import com.dain_review.domain.campaign.model.entity.enums.Label;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import com.dain_review.domain.campaign.model.entity.enums.Type;
import com.dain_review.domain.campaign.model.request.CampaignRequest;
import com.dain_review.domain.campaign.util.AddressAndPointUtil;
import com.dain_review.domain.choice.model.entity.Choice;
import com.dain_review.domain.review.model.entity.Review;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.CascadeType;
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
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Setter
    @OneToMany(
            mappedBy = "campaign",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST) // Cascade 추가
    private Set<AvailableDay> availableDays; // 체험 가능 요일

    @Setter
    @OneToMany(
            mappedBy = "campaign",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST) // Cascade 추가
    private Set<Keyword> keywords; // 홍보용 키워드(태그)

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

    public static Campaign create(User user, String imageUrl, CampaignRequest request) {
        Campaign campaign = new Campaign();
        campaign.user = user;
        campaign.imageUrl = imageUrl;
        campaign.businessName = request.businessName();
        campaign.contactNumber = request.contactNumber();
        campaign.postalCode = request.postalCode();
        campaign.setAddress(request.address());
        campaign.latitude = request.latitude();
        campaign.longitude = request.longitude();
        campaign.platform = request.platform();
        campaign.type = request.type();
        campaign.category = request.category();
        campaign.pointPayment = request.pointPayment();
        campaign.capacity = request.capacity();
        campaign.pointPerPerson = request.pointPerPerson();
        campaign.serviceProvided = request.serviceProvided();
        campaign.requirement = request.requirement();
        campaign.applicationStartDate = request.applicationStartDate();
        campaign.applicationEndDate = request.applicationEndDate();
        campaign.announcementDate = request.announcementDate();
        campaign.experienceStartDate = request.experienceStartDate();
        campaign.experienceEndDate = request.experienceEndDate();
        campaign.reviewDate = request.reviewDate();
        campaign.campaignState = CampaignState.INSPECTION; // 기본값
        campaign.isDeleted = false; // 기본값

        campaign.label = Boolean.TRUE.equals(request.pointPayment()) ? Label.PREMIUM : null;

        Set<AvailableDay> availableDays =
                request.availableDays().stream()
                        .map(day -> new AvailableDay(campaign, day))
                        .collect(Collectors.toSet());
        campaign.setAvailableDays(availableDays);

        Set<Keyword> keywords =
                request.keywords().stream()
                        .map(keyword -> new Keyword(campaign, keyword))
                        .collect(Collectors.toSet());
        campaign.setKeywords(keywords);

        campaign.calculateAndSetTotalPoints();
        return campaign;
    }

    public void calculateAndSetTotalPoints() {
        this.totalPoints =
                AddressAndPointUtil.calculateTotalPoints(this.capacity, this.pointPerPerson);
    }

    public boolean isCancelable() {
        return this.campaignState == CampaignState.RECRUITING
                || this.campaignState == CampaignState.RECRUITMENT_COMPLETED
                || this.campaignState == CampaignState.EXPERIENCE_AND_REVIEW;
    }

    // 캠페인 삭제 메서드 (취소 가능 여부와 소유자 검증 포함)
    public void delete(User user) {
        // 소유자 검증
        if (!this.user.getId().equals(user.getId())) {
            throw new CampaignException(CampaignErrorCode.UNAUTHORIZED_ACCESS);
        }
        // 취소 가능 여부 확인
        if (!isCancelable()) {
            throw new CampaignException(CampaignErrorCode.CANNOT_DELETE_CAMPAIGN);
        }
        // 캠페인 삭제 처리
        this.isDeleted = true;
    }

    public void setAddress(String address) {
        this.address = address;
        String[] cityAndDistrict = AddressAndPointUtil.extractCityAndDistrict(address);
        this.city = cityAndDistrict[0];
        this.district = cityAndDistrict[1];
    }

    public boolean isNotReviewPeriod() {
        return LocalDateTime.now().isAfter(reviewDate) || LocalDateTime.now().isBefore(experienceStartDate);
    }
}
