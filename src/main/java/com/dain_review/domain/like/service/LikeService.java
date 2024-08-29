package com.dain_review.domain.like.service;


import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.domain.like.model.entity.Like;
import com.dain_review.domain.like.repository.LikeRepository;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.exception.errortype.UserErrorCode;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.util.S3Util;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final LikeCacheService likeCacheService;
    private final S3Util s3Util;

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
        Page<Like> likes = likeRepository.findByUserIdAndIsLiked(userId, true, pageable);

        List<CampaignResponse> content =
                likes.stream()
                        .map(
                                like ->
                                        CampaignResponse.fromEntity(
                                                like.getCampaign(),
                                                s3Util.selectImage(
                                                        like.getCampaign().getImageUrl())))
                        .toList();

        return new PagedResponse<>(content, likes.getTotalElements(), likes.getTotalPages());
    }

    @Transactional(readOnly = true)
    public Boolean getLikeStatus(Long userId, Long campaignId) {
        return likeCacheService.readThroughLikeStatus(userId, campaignId, likeRepository);
    }
}
