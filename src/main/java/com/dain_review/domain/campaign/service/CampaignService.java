package com.dain_review.domain.campaign.service;

import static com.dain_review.global.util.ImageFileValidUtil.isValidImageFile;

import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.State;
import com.dain_review.domain.campaign.model.request.CampaignRequest;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.exception.errortype.UserErrorCode;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.util.S3Util;
import com.dain_review.global.util.error.S3Exception;
import com.dain_review.global.util.errortype.S3ErrorCode;
import lombok.RequiredArgsConstructor;
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

    private User getUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    public CampaignResponse createCampaign(
            Long userId, CampaignRequest campaignRequest, MultipartFile imageFile) {
        User user = getUser(userId);

        // 이미지 업로드 처리
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            if (!isValidImageFile(imageFile)) {
                throw new S3Exception(S3ErrorCode.INVALID_IMAGE_FILE);
            }
            imageUrl = s3Util.saveImage(imageFile).join();
        }

        Integer totalPoints = null;
        Integer pointPerPerson = null;

        // 총 지급 포인트 계산
        if (Boolean.TRUE.equals(campaignRequest.pointPayment())) {
            pointPerPerson = campaignRequest.pointPerPerson();
            totalPoints = calculateTotalPoints(campaignRequest.capacity(), pointPerPerson);
        }

        Campaign campaign =
                Campaign.builder()
                        .user(user)
                        .imageUrl(imageUrl)
                        .businessName(campaignRequest.businessName())
                        .contactNumber(campaignRequest.contactNumber())
                        .address(campaignRequest.address())
                        .latitude(campaignRequest.latitude())
                        .longitude(campaignRequest.longitude())
                        .availableDays(campaignRequest.availableDays())
                        .type(campaignRequest.type())
                        .category(campaignRequest.category())
                        .platform(campaignRequest.platform())
                        .capacity(campaignRequest.capacity())
                        .serviceProvided(campaignRequest.serviceProvided())
                        .keywords(campaignRequest.keywords())
                        .pointPayment(campaignRequest.pointPayment())
                        .pointPerPerson(campaignRequest.pointPerPerson())
                        .totalPoints(totalPoints)
                        .applicationStartDate(campaignRequest.applicationStartDate())
                        .applicationEndDate(campaignRequest.applicationEndDate())
                        .announcementDate(campaignRequest.announcementDate())
                        .experienceStartDate(campaignRequest.experienceStartDate())
                        .experienceEndDate(campaignRequest.experienceEndDate())
                        .reviewDate(campaignRequest.reviewDate())
                        .state(State.INSPECTION) // 기본 상태를 "검수중"으로 설정
                        .isDeleted(false) // 기본 값은 삭제되지 않음
                        .build();

        campaignRepository.save(campaign);

        return CampaignResponse.fromEntity(campaign);
    }

    private Integer calculateTotalPoints(Integer capacity, Integer pointPerPerson) {
        /*총 포인트 계산 메서드*/
        return (int) Math.round(capacity * pointPerPerson * 1.2);
    }

    public CampaignResponse updateCampaign(
            Long userId,
            Long campaignId,
            CampaignRequest campaignRequest,
            MultipartFile imageFile) {
        User user = getUser(userId);
        Campaign campaign = getCampaignEntity(campaignId, user);

        String imageUrl = campaign.getImageUrl();
        if (imageFile != null && !imageFile.isEmpty()) {
            if (!isValidImageFile(imageFile)) {
                throw new S3Exception(S3ErrorCode.INVALID_IMAGE_FILE);
            }
            imageUrl = s3Util.saveImage(imageFile).join();
        }

        Integer totalPoints = null;
        if (Boolean.TRUE.equals(campaignRequest.pointPayment())) {
            Integer pointPerPerson = campaignRequest.pointPerPerson();
            totalPoints = calculateTotalPoints(campaignRequest.capacity(), pointPerPerson);
        }

        campaign.updateCampaign(
                imageUrl,
                campaignRequest.businessName(),
                campaignRequest.contactNumber(),
                campaignRequest.address(),
                campaignRequest.latitude(),
                campaignRequest.longitude(),
                campaignRequest.availableDays(),
                campaignRequest.type(),
                campaignRequest.category(),
                campaignRequest.platform(),
                campaignRequest.capacity(),
                campaignRequest.serviceProvided(),
                campaignRequest.keywords(),
                campaignRequest.pointPayment(),
                campaignRequest.pointPerPerson(),
                totalPoints,
                campaignRequest.applicationStartDate(),
                campaignRequest.applicationEndDate(),
                campaignRequest.announcementDate(),
                campaignRequest.experienceStartDate(),
                campaignRequest.experienceEndDate(),
                campaignRequest.reviewDate());

        campaignRepository.save(campaign);

        return CampaignResponse.fromEntity(campaign);
    }

    public void deleteCampaign(Long userId, Long campaignId) {
        User user = getUser(userId);
        Campaign campaign = getCampaignEntity(campaignId, user);
        campaign.markAsDeleted();
        campaignRepository.save(campaign);
    }

    public CampaignResponse getCampaign(Long userId, Long campaignId) {
        User user = getUser(userId);
        Campaign campaign = getCampaignEntity(campaignId, user);
        return CampaignResponse.fromEntity(campaign);
    }

    private Campaign getCampaignEntity(Long campaignId, User user) {
        return campaignRepository
                .findById(campaignId)
                .filter(campaign -> campaign.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new CampaignException(CampaignErrorCode.CAMPAIGN_NOT_FOUND));
    }
}
