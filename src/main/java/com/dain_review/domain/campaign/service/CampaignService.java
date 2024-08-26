package com.dain_review.domain.campaign.service;

import static com.dain_review.global.util.ImageFileValidUtil.isValidImageFile;

import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.Label;
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
        String imageFileName = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            if (!isValidImageFile(imageFile)) {
                throw new S3Exception(S3ErrorCode.INVALID_IMAGE_FILE);
            }
            imageFileName = s3Util.saveImage(imageFile).join();
        }

        Integer totalPoints = null;
        Integer pointPerPerson;

        // 총 지급 포인트 계산
        if (Boolean.TRUE.equals(campaignRequest.pointPayment())) {
            pointPerPerson = campaignRequest.pointPerPerson();
            totalPoints = calculateTotalPoints(campaignRequest.capacity(), pointPerPerson);
        }

        // 라벨 설정
        Label label =
                Boolean.TRUE.equals(campaignRequest.pointPayment())
                        ? Label.PREMIUM
                        : null; // 일단 기본 값은 null로 지정

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
                        .availableDays(campaignRequest.availableDays())
                        .type(campaignRequest.type())
                        .category(campaignRequest.category())
                        .platform(campaignRequest.platform())
                        .label(label)
                        .city(city)
                        .district(district)
                        .capacity(campaignRequest.capacity())
                        .serviceProvided(campaignRequest.serviceProvided())
                        .requirement(campaignRequest.requirement())
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

        return convertToCampaignResponse(campaign);
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
        /*총 포인트 계산*/
        /*총 포인트 계산 메서드*/
        return (int) Math.round(capacity * pointPerPerson * 1.2);
    }

    private CampaignResponse convertToCampaignResponse(Campaign campaign) {
        /*이미지 이름 -> 이미지 url*/
        // S3 URL로 변환
        String imageUrl =
                (campaign.getImageUrl() != null)
                        ? s3Util.selectImage(campaign.getImageUrl())
                        : null;

        return CampaignResponse.fromEntity(campaign, imageUrl);
    }

    // 체험단 단건 조회
    @Transactional(readOnly = true)
    public CampaignResponse getCampaignById(Long campaignId) {
        Campaign campaign =
                campaignRepository
                        .findById(campaignId)
                        .orElseThrow(
                                () -> new CampaignException(CampaignErrorCode.CAMPAIGN_NOT_FOUND));

        return convertToCampaignResponse(campaign);
    }
}
