package com.dain_review.domain.campaign.service;

import static com.dain_review.global.util.ImageFileValidUtil.isValidImageFile;

import com.dain_review.domain.application.model.response.ApplicantResponse;
import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.entity.enums.Label;
import com.dain_review.domain.campaign.model.request.CampaignFilterRequest;
import com.dain_review.domain.campaign.model.request.CampaignRequest;
import com.dain_review.domain.campaign.model.request.CampaignSearchRequest;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.campaign.model.response.CampaignSummaryResponse;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.domain.choice.model.response.ChoiceInfluencerResponse;
import com.dain_review.domain.review.model.response.ReviewerResponse;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.type.S3PathPrefixType;
import com.dain_review.global.util.S3Util;
import com.dain_review.global.util.error.S3Exception;
import com.dain_review.global.util.errortype.S3ErrorCode;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final S3Util s3Util;

    private final String S3_PATH_PREFIX = S3PathPrefixType.S3_CAMPAIGN_THUMBNAIL_PATH.toString();

    public CampaignResponse createCampaign(
            Long userId, CampaignRequest campaignRequest, MultipartFile imageFile) {

        User user = userRepository.getUserById(userId);

        // 이미지 업로드 처리

        if (imageFile == null || imageFile.isEmpty()) {
            throw new CampaignException(CampaignErrorCode.IMAGE_REQUIRED);
        }
        if (!isValidImageFile(imageFile)) {
            throw new S3Exception(S3ErrorCode.INVALID_IMAGE_FILE);
        }

        String imageFileName = s3Util.saveImage(imageFile, S3_PATH_PREFIX).join();

        Integer totalPoints = null;
        Integer pointPerPerson = null;

        // 총 지급 포인트 계산
        if (Boolean.TRUE.equals(campaignRequest.pointPayment())) {
            pointPerPerson = campaignRequest.pointPerPerson();
            totalPoints = calculateTotalPoints(campaignRequest.capacity(), pointPerPerson);
        }

        // 라벨 설정
        Label label = Boolean.TRUE.equals(campaignRequest.pointPayment()) ? Label.PREMIUM : null;

        // 주소에서 시/도, 구/군 추출
        String[] cityAndDistrict = extractCityAndDistrict(campaignRequest.address());
        String city = cityAndDistrict[0];
        String district = cityAndDistrict[1];

        Campaign campaign =
                Campaign.builder()
                        .user(user)
                        .imageUrl(imageFileName)
                        .businessName(campaignRequest.businessName())
                        .contactNumber(campaignRequest.contactNumber())
                        .address(campaignRequest.address())
                        .latitude(campaignRequest.latitude())
                        .longitude(campaignRequest.longitude())
                        .availableDays(
                                new HashSet<>(campaignRequest.availableDays())) // List를 Set으로 변환
                        .type(campaignRequest.type())
                        .category(campaignRequest.category())
                        .platform(campaignRequest.platform())
                        .label(label)
                        .city(city)
                        .district(district)
                        .capacity(campaignRequest.capacity())
                        .serviceProvided(campaignRequest.serviceProvided())
                        .requirement(campaignRequest.requirement())
                        .keywords(new HashSet<>(campaignRequest.keywords())) // List를 Set으로 변환
                        .pointPayment(campaignRequest.pointPayment())
                        .pointPerPerson(campaignRequest.pointPerPerson())
                        .totalPoints(totalPoints)
                        .applicationStartDate(campaignRequest.applicationStartDate())
                        .applicationEndDate(campaignRequest.applicationEndDate())
                        .announcementDate(campaignRequest.announcementDate())
                        .experienceStartDate(campaignRequest.experienceStartDate())
                        .experienceEndDate(campaignRequest.experienceEndDate())
                        .reviewDate(campaignRequest.reviewDate())
                        .campaignState(CampaignState.INSPECTION) // 기본 상태를 "검수중"으로 설정
                        .isDeleted(false) // 기본 값은 삭제되지 않음
                        .build();

        campaignRepository.save(campaign);
        return CampaignResponse.fromEntity(
                campaign, s3Util.selectImage(campaign.getImageUrl(), S3_PATH_PREFIX));
    }

    @Transactional(readOnly = true)
    public CampaignResponse getCampaignById(Long campaignId) { // 체험단 단건 조회
        Campaign campaign =
                campaignRepository
                        .findWithDetailsById(campaignId)
                        .orElseThrow(
                                () -> new CampaignException(CampaignErrorCode.CAMPAIGN_NOT_FOUND));

        return CampaignResponse.fromEntity(
                campaign, s3Util.selectImage(campaign.getImageUrl(), S3_PATH_PREFIX));
    }

    public void deleteCampaign(Long userId, Long campaignId) { // 체험단 삭제(취소)
        User user = userRepository.getUserById(userId);
        Campaign campaign = campaignRepository.getCampaignById(campaignId);

        if (!campaign.getUser().getId().equals(user.getId())) {
            throw new CampaignException(CampaignErrorCode.UNAUTHORIZED_ACCESS);
        }
        campaign.setIsDeleted(true);
    }

    // 사업주가 등록한 체험단 목록 조회
    public Page<CampaignSummaryResponse> getRegisteredCampaigns(
            CampaignFilterRequest campaignFilterRequest, Pageable pageable, Long userId) {

        Page<Campaign> campaignPage =
                campaignRepository.findByStateAndPlatformAndNameContainingAndUserId(
                        campaignFilterRequest.campaignState(),
                        campaignFilterRequest.platform(),
                        campaignFilterRequest.keyword(),
                        userId,
                        pageable);

        return campaignPage.map(
                campaign ->
                        CampaignSummaryResponse.from(
                                campaign,
                                s3Util.selectImage(campaign.getImageUrl(), S3_PATH_PREFIX)));
    }

    // 체험단 검색
    public PagedResponse<CampaignSummaryResponse> searchCampaigns(
            CampaignSearchRequest searchRequest, Pageable pageable) {
        Page<Campaign> campaignPage = campaignRepository.searchCampaigns(searchRequest, pageable);
        List<CampaignSummaryResponse> content =
                campaignPage
                        .map(
                                campaign ->
                                        CampaignSummaryResponse.from(
                                                campaign,
                                                s3Util.selectImage(
                                                        campaign.getImageUrl(), S3_PATH_PREFIX)))
                        .getContent();

        return new PagedResponse<>(
                content, campaignPage.getTotalElements(), campaignPage.getTotalPages());
    }

    private String[] extractCityAndDistrict(String address) {
        String[] addressParts = address.split(" ");
        String rawCity = addressParts[0]; // 시/도

        // "서울특별시" -> "서울", "부산광역시" -> "부산" 등으로 변환
        String city = rawCity.replace("특별시", "").replace("광역시", "").replace("도", "");

        String district = addressParts[1]; // 구/군

        return new String[] {city, district};
    }

    private Integer calculateTotalPoints(Integer capacity, Integer pointPerPerson) {
        /*총포인트 계산*/
        return (int) Math.round(capacity * pointPerPerson * 1.2);
    }

    @Transactional
    public List<ApplicantResponse> getApplicants(Long campaignId, Long userId) {
        Campaign campaign = campaignRepository.getCampaignById(campaignId);
        return ApplicantResponse.of(campaign, userId);
    }

    @Transactional
    public List<ChoiceInfluencerResponse> getSelectedInfluencers(Long campaignId, Long userId) {
        Campaign campaign = campaignRepository.getCampaignById(campaignId);
        return ChoiceInfluencerResponse.of(campaign, userId);
    }

    @Transactional
    public List<ReviewerResponse> getReviews(Long campaignId, Long userId) {
        Campaign campaign = campaignRepository.getCampaignById(campaignId);
        return ReviewerResponse.of(campaign, userId);
    }
}
