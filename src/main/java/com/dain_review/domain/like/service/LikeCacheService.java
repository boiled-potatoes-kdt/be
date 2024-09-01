package com.dain_review.domain.like.service;


import com.dain_review.domain.like.model.entity.Like;
import com.dain_review.domain.like.repository.LikeRepository;
import com.dain_review.global.type.RedisPrefixType;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LIKE_KEY_PREFIX = RedisPrefixType.LIKE.toString();

    public void cacheLikeStatus(Long userId, Long campaignId, boolean isLiked) {
        String key = LIKE_KEY_PREFIX + userId + "::" + campaignId;
        redisTemplate.opsForValue().set(key, isLiked, 1, TimeUnit.HOURS); // 1시간 TTL
    }

    public Boolean getLikeStatus(Long userId, Long campaignId) {
        String key = LIKE_KEY_PREFIX + userId + "::" + campaignId;
        return (Boolean) redisTemplate.opsForValue().get(key);
    }

    public Boolean readThroughLikeStatus(
            Long userId, Long campaignId, LikeRepository likeRepository) {
        String key = LIKE_KEY_PREFIX + userId + "::" + campaignId;
        Boolean cachedLikeStatus = (Boolean) redisTemplate.opsForValue().get(key);
        if (cachedLikeStatus != null) {
            return cachedLikeStatus;
        }

        Like like = likeRepository.findByUserIdAndCampaignId(userId, campaignId).orElse(null);
        if (like == null) {
            return false;
        }

        boolean isLiked = like.isLiked();
        redisTemplate.opsForValue().set(key, isLiked, 1, TimeUnit.HOURS);
        return isLiked;
    }

    public Set<String> getLikeKeys() {
        return redisTemplate.keys(LIKE_KEY_PREFIX + "*");
    }

    public Long[] extractIdsFromKey(String key) {
        String[] parts = key.replace(LIKE_KEY_PREFIX, "").split("::");
        long userId = Long.parseLong(parts[0]);
        long campaignId = Long.parseLong(parts[1]);
        return new Long[] {userId, campaignId};
    }

    public Set<String> getLikeKeysForUser(Long userId) {
        return redisTemplate.keys(LIKE_KEY_PREFIX + userId + "::" + "*");
    }
}
