package com.dain_review.domain.campaign.service;


import com.dain_review.domain.Image.service.ImageFileService;
import com.dain_review.domain.application.model.response.ApplicantResponse;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.request.CampaignFilterRequest;
import com.dain_review.domain.campaign.model.request.CampaignRequest;
import com.dain_review.domain.campaign.model.request.CampaignSearchRequest;
import com.dain_review.domain.campaign.model.response.CampaignHomeResponse;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.campaign.model.response.CampaignSummaryResponse;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.domain.choice.model.response.ChoiceInfluencerResponse;
import com.dain_review.domain.review.model.response.ReviewerResponse;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.type.S3PathPrefixType;
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
    private final ImageFileService imageFileService;

    public CampaignResponse createCampaign(
            Long userId, CampaignRequest campaignRequest, MultipartFile imageFile) {
        User user = userRepository.getUserById(userId);

        String imageFileName =
                imageFileService.validateAndUploadImage(
                        imageFile, S3PathPrefixType.S3_CAMPAIGN_THUMBNAIL_PATH);

        String imageUrl =
                imageFileService.getImageUrl(
                        imageFileName, S3PathPrefixType.S3_CAMPAIGN_THUMBNAIL_PATH);

        // 캠페인 생성
        Campaign campaign = Campaign.create(user, imageFileName, imageUrl, campaignRequest);
        campaignRepository.save(campaign);

        return CampaignResponse.from(campaign);
    }

    @Transactional(readOnly = true)
    public CampaignResponse getCampaignById(Long campaignId) { // 체험단 단건 조회
        Campaign campaign = campaignRepository.getCampaignById(campaignId);

        return CampaignResponse.from(campaign);
    }

    public void deleteCampaign(Long userId, Long campaignId) { // 체험단 삭제(취소)
        User user = userRepository.getUserById(userId);
        Campaign campaign = campaignRepository.getCampaignById(campaignId);

        campaign.delete(user);
    }

    // 사업주가 등록한 체험단 목록 조회
    @Transactional(readOnly = true)
    public Page<CampaignSummaryResponse> getRegisteredCampaigns(
            CampaignFilterRequest campaignFilterRequest, Pageable pageable, Long userId) {

        Page<Campaign> campaignPage =
                campaignRepository.findByStateAndPlatformAndNameContainingAndUserId(
                        campaignFilterRequest.campaignState(),
                        campaignFilterRequest.platform(),
                        campaignFilterRequest.keyword(),
                        userId,
                        pageable);

        return campaignPage.map(CampaignSummaryResponse::from);
    }

    // 체험단 검색
    @Transactional(readOnly = true)
    public PagedResponse<CampaignSummaryResponse> searchCampaigns(
            CampaignSearchRequest searchRequest, Pageable pageable) {
        Page<Campaign> campaignPage = campaignRepository.searchCampaigns(searchRequest, pageable);
        List<CampaignSummaryResponse> content =
                campaignPage.map(CampaignSummaryResponse::from).getContent();

        return new PagedResponse<>(
                content, campaignPage.getTotalElements(), campaignPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public List<ApplicantResponse> getApplicants(Long campaignId, Long userId) {
        Campaign campaign = campaignRepository.getCampaignById(campaignId);
        return ApplicantResponse.of(campaign, userId);
    }

    @Transactional(readOnly = true)
    public List<ChoiceInfluencerResponse> getSelectedInfluencers(Long campaignId, Long userId) {
        Campaign campaign = campaignRepository.getCampaignById(campaignId);
        return ChoiceInfluencerResponse.of(campaign, userId);
    }

    @Transactional(readOnly = true)
    public List<ReviewerResponse> getReviews(Long campaignId, Long userId) {
        Campaign campaign = campaignRepository.getCampaignById(campaignId);
        return ReviewerResponse.of(campaign, userId);
    }

    @Transactional(readOnly = true)
    public CampaignHomeResponse getCampaignForHomeScreen() {
        List<Campaign> premium = campaignRepository.findPremiumCampaigns();
        List<Campaign> popular = campaignRepository.findPopularCampaigns();
        List<Campaign> newest = campaignRepository.findNewestCampaigns();
        List<Campaign> imminent = campaignRepository.findImminentDueDateCampaigns();

        return new CampaignHomeResponse(
                mapToSummaryResponses(premium),
                mapToSummaryResponses(popular),
                mapToSummaryResponses(newest),
                mapToSummaryResponses(imminent));
    }

    private List<CampaignSummaryResponse> mapToSummaryResponses(List<Campaign> campaigns) {
        return campaigns.stream().map(CampaignSummaryResponse::from).toList();
    }
}
