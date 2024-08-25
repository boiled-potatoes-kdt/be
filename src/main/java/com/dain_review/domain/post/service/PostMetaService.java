package com.dain_review.domain.post.service;


import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.global.type.RedisPrefixType;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostMetaService {

    private final PostRepository postRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = RedisPrefixType.POST_VIEW_COUNT.toString();

    public void incrementViewCount(Long postId) {
        String key = PREFIX + postId;
        redisTemplate.opsForValue().increment(key, 1); // 원자적으로 조회수 증가
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void syncViewCountToDB() {
        Set<String> keys = redisTemplate.keys(PREFIX + "*");
        if (keys == null || keys.isEmpty()) {
            return;
        }
        keys.forEach(this::updatePostViewCount);
    }

    private void updatePostViewCount(String key) {
        try {
            Long postId = extractPostIdFromKey(key);
            String viewCountStr = redisTemplate.opsForValue().get(key); // String으로 가져옴

            if (viewCountStr != null) {
                int viewCount = Integer.parseInt(viewCountStr); // String -> Integer 변환
                redisTemplate.delete(key);

                postRepository.updateViewCount(postId, viewCount);
            }
        } catch (Exception e) {
            log.error("동기화 실패: " + key, e);
        }
    }

    private Long extractPostIdFromKey(String key) {
        return Long.valueOf(key.replace(PREFIX, ""));
    }
}
