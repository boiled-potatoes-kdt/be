package com.dain_review.domain.like.service;


import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.domain.like.model.entity.Like;
import com.dain_review.domain.like.repository.LikeRepository;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import java.util.List;
import java.util.Optional;
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

    @Transactional
    public boolean toggleLike(Long userId, Long campaignId) {
        User user = userRepository.getUserById(userId);
        Campaign campaign = campaignRepository.getCampaignById(campaignId);

        Optional<Like> existingLike = likeRepository.findByUserIdAndCampaignId(userId, campaignId);
        if (existingLike.isPresent()) {
            // 기존 좋아요 상태 토글
            Like like = existingLike.get().withToggleStatus();
            likeRepository.save(like);
            return like.isLiked();
        } else {
            // 좋아요가 없을 경우 새로 생성
            Like newLike = Like.create(user, campaign);
            likeRepository.save(newLike);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public PagedResponse<CampaignResponse> getLikedCampaigns(Long userId, Pageable pageable) {
        Page<Like> likes = likeRepository.findByUserIdAndIsLiked(userId, true, pageable);

        List<CampaignResponse> content =
                likes.stream()
                        .map(
                                like -> {
                                    return CampaignResponse.from(like.getCampaign());
                                })
                        .toList();

        return new PagedResponse<>(content, likes.getTotalElements(), likes.getTotalPages());
    }
}
