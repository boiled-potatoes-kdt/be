package com.dain_review.domain.like.service;


import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.domain.like.repository.LikeRepository;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.exception.errortype.UserErrorCode;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.type.S3PathPrefixType;
import com.dain_review.global.util.S3Util;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final LikeCacheService likeCacheService;
    private final S3Util s3Util;

    private final String S3_PATH_PREFIX = S3PathPrefixType.S3_CAMPAIGN_THUMBNAIL_PATH.toString();

    @Transactional
    public boolean toggleLike(Long userId, Long campaignId) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        campaignRepository
                .findById(campaignId)
                .orElseThrow(() -> new CampaignException(CampaignErrorCode.CAMPAIGN_NOT_FOUND));

        Boolean currentStatus = getLikeStatus(userId, campaignId);
        boolean newStatus = !currentStatus;

        // 캐시 갱신
        likeCacheService.cacheLikeStatus(userId, campaignId, newStatus);

        return newStatus;
    }

    @Transactional(readOnly = true)
    public PagedResponse<CampaignResponse> getLikedCampaigns(Long userId, Pageable pageable) {
        // 1. Redis에서 사용자에 대한 모든 좋아요 상태를 조회
        Set<String> likedCampaignKeys = likeCacheService.getLikeKeysForUser(userId);

        // 2. 좋아요된 캠페인 ID 추출 및 필터링 (캐시에 저장된 데이터 사용)
        List<Long> likedCampaignIds =
                likedCampaignKeys.stream()
                        .map(key -> likeCacheService.extractIdsFromKey(key)[1]) // campaignId 추출
                        .filter(
                                campaignId ->
                                        Boolean.TRUE.equals(
                                                likeCacheService.getLikeStatus(
                                                        userId, campaignId))) // true인 캠페인만 필터링
                        .toList();

        // 3. 필요한 캠페인 ID들을 기준으로 DB에서 조회
        if (likedCampaignIds.isEmpty()) {
            return new PagedResponse<>(List.of(), 0L, 0);
        }

        Page<Campaign> campaigns = campaignRepository.findByIdIn(likedCampaignIds, pageable);

        // 4. CampaignResponse로 변환하여 결과 반환
        List<CampaignResponse> content =
                campaigns.stream()
                        .map(
                                campaign ->
                                        CampaignResponse.fromEntity(
                                                campaign,
                                                s3Util.selectImage(
                                                        campaign.getImageUrl(), S3_PATH_PREFIX)))
                        .toList();

        return new PagedResponse<>(
                content, campaigns.getTotalElements(), campaigns.getTotalPages());
    }

    @Transactional(readOnly = true)
    public Boolean getLikeStatus(Long userId, Long campaignId) {
        return likeCacheService.readThroughLikeStatus(userId, campaignId, likeRepository);
    }
}
