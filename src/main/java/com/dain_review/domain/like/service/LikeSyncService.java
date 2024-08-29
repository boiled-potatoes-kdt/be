package com.dain_review.domain.like.service;


import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.domain.like.model.entity.Like;
import com.dain_review.domain.like.repository.LikeRepository;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.exception.errortype.UserErrorCode;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeSyncService {

    private final LikeRepository likeRepository;
    private final LikeCacheService likeCacheService;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;

    @Scheduled(fixedDelay = 60000) // 1분마다 실행
    public void syncLikesToDB() {
        Set<String> keys = likeCacheService.getLikeKeys();
        if (keys == null || keys.isEmpty()) {
            return;
        }
        keys.forEach(this::updateLikeStatus);
    }

    private void updateLikeStatus(String key) {
        Long[] ids = likeCacheService.extractIdsFromKey(key);
        Long userId = ids[0];
        Long campaignId = ids[1];
        Boolean isLiked = likeCacheService.getLikeStatus(userId, campaignId);

        if (isLiked != null) {
            likeRepository
                    .findByUserIdAndCampaignId(userId, campaignId)
                    .ifPresentOrElse(
                            like -> {
                                like.setLiked(isLiked);
                                likeRepository.save(like);
                            },
                            () -> {
                                if (isLiked) {
                                    User user =
                                            userRepository
                                                    .findById(userId)
                                                    .orElseThrow(
                                                            () ->
                                                                    new UserException(
                                                                            UserErrorCode
                                                                                    .USER_NOT_FOUND));
                                    Campaign campaign =
                                            campaignRepository
                                                    .findById(campaignId)
                                                    .orElseThrow(
                                                            () ->
                                                                    new CampaignException(
                                                                            CampaignErrorCode
                                                                                    .CAMPAIGN_NOT_FOUND));

                                    likeRepository.save(
                                            Like.builder()
                                                    .user(user)
                                                    .campaign(campaign)
                                                    .isLiked(true)
                                                    .build());
                                }
                            });
        }
    }
}
