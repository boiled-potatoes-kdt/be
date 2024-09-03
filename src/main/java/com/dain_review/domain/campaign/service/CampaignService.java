package com.dain_review.domain.campaign.service;

import static com.dain_review.domain.Image.util.ImageFileValidUtil.isValidImageFile;

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
import com.dain_review.domain.review.model.response.ReviewerResponse;
import com.dain_review.domain.select.model.response.SelectedInfluencerResponse;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.type.S3PathPrefixType;
import com.dain_review.global.util.S3Util;
import com.dain_review.domain.Image.exception.S3Exception;
import com.dain_review.domain.Image.exception.errortype.S3ErrorCode;
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

        String imageFileName = s3Util.saveImage(imageFile, S3PathPrefixType.S3_CAMPAIGN_THUMBNAIL_PATH.toString()).join();

        // 캠페인 생성
        Campaign campaign =
            Campaign.builder()
                .user(user)
                .imageUrl(imageFileName)
                .businessName(campaignRequest.businessName())
                .contactNumber(campaignRequest.contactNumber())
                .address(campaignRequest.address())
                .latitude(campaignRequest.latitude())
                .longitude(campaignRequest.longitude())
                .availableDays(new HashSet<>(campaignRequest.availableDays())) // List를 Set으로 변환
                .type(campaignRequest.type())
                .category(campaignRequest.category())
                .platform(campaignRequest.platform())
                .label(Boolean.TRUE.equals(campaignRequest.pointPayment()) ? Label.PREMIUM : null)
                .capacity(campaignRequest.capacity())
                .pointPayment(campaignRequest.pointPayment())
                .pointPerPerson(campaignRequest.pointPerPerson())
                .applicationStartDate(campaignRequest.applicationStartDate())
                .applicationEndDate(campaignRequest.applicationEndDate())
                .announcementDate(campaignRequest.announcementDate())
                .experienceStartDate(campaignRequest.experienceStartDate())
                .experienceEndDate(campaignRequest.experienceEndDate())
                .reviewDate(campaignRequest.reviewDate())
                .campaignState(CampaignState.INSPECTION) // 기본 상태를 "검수중"으로 설정
                .isDeleted(false) // 기본 값은 삭제되지 않음
                .build();

        campaign.setAddress(campaignRequest.address());
        campaign.calculateAndSetTotalPoints();

        campaignRepository.save(campaign);
        return CampaignResponse.fromEntity(
            campaign, s3Util.selectImage(campaign.getImageUrl(), S3PathPrefixType.S3_CAMPAIGN_THUMBNAIL_PATH.toString()));
    }

    @Transactional(readOnly = true)
    public CampaignResponse getCampaignById(Long campaignId) { // 체험단 단건 조회
        Campaign campaign =
            campaignRepository
                .findWithDetailsById(campaignId)
                .orElseThrow(
                    () -> new CampaignException(CampaignErrorCode.CAMPAIGN_NOT_FOUND));

        return CampaignResponse.fromEntity(
            campaign, s3Util.selectImage(campaign.getImageUrl(), S3PathPrefixType.S3_CAMPAIGN_THUMBNAIL_PATH.toString()));
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
                    s3Util.selectImage(campaign.getImageUrl(), S3PathPrefixType.S3_CAMPAIGN_THUMBNAIL_PATH.toString())));
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
                                campaign.getImageUrl(), S3PathPrefixType.S3_CAMPAIGN_THUMBNAIL_PATH.toString())))
                .getContent();

        return new PagedResponse<>(
            content, campaignPage.getTotalElements(), campaignPage.getTotalPages());
    }

    public List<ApplicantResponse> getApplicants(Long campaignId, Long userId) {
        Campaign campaign = campaignRepository.getCampaignById(campaignId);

        return ApplicantResponse.from(campaign, userId);
    }

    public List<SelectedInfluencerResponse> getSelectedInfluencers(Long campaignId, Long userId) {
        Campaign campaign = campaignRepository.getCampaignById(campaignId);
        return SelectedInfluencerResponse.from(campaign, userId);
    }

    public List<ReviewerResponse> getReviews(Long campaignId, Long userId) {
        Campaign campaign = campaignRepository.getCampaignById(campaignId);
        return ReviewerResponse.from(campaign, userId);
    }
}
