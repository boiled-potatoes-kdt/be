package com.dain_review.domain.post.service;


import com.dain_review.domain.post.exception.PostException;
import com.dain_review.domain.post.exception.errorcode.PostErrorCode;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.model.entity.PostMeta;
import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.global.type.RedisPrefixType;
import jakarta.persistence.OptimisticLockException;
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
public class ViewCountService {

    private final PostRepository postRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final RedisPrefixType PREFIX_TYPE = RedisPrefixType.POST_VIEW_COUNT;

    public void incrementViewCount(Long postId) {
        String key = PREFIX_TYPE.toString() + postId;
        redisTemplate.opsForValue().increment(key);
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void syncViewCountToDB() {
        Set<String> keys = redisTemplate.keys(PREFIX_TYPE + "*");
        if (keys == null || keys.isEmpty()) {
            return;
        }

        keys.forEach(
                key -> {
                    try {
                        String viewCountStr = redisTemplate.opsForValue().get(key);
                        if (viewCountStr != null) {
                            int viewCount = Integer.parseInt(viewCountStr);
                            Long postId = Long.valueOf(key.replace(PREFIX_TYPE.toString(), ""));

                            Post post =
                                    postRepository
                                            .findById(postId)
                                            .orElseThrow(
                                                    () ->
                                                            new PostException(
                                                                    PostErrorCode.POST_NOT_FOUND));

                            PostMeta postMeta = post.getPostMeta();
                            postMeta.setViewCount(postMeta.getViewCount() + viewCount);
                            postRepository.save(post);

                            redisTemplate.delete(key);
                        }
                    } catch (OptimisticLockException e) {
                        log.warn("낙관적 락 충돌이 발생했습니다. 다시 시도합니다. key: {}", key);
                    } catch (Exception e) {
                        log.error("동기화 중 오류 발생 key: {}", key, e);
                    }
                });
    }
}
