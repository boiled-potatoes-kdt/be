package com.dain_review.global.scheduler;


import com.dain_review.domain.post.service.PostMetaService;
import com.dain_review.global.type.RedisPrefixType;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ViewCommentScheduler {

    private final PostMetaService postMetaService;
    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(fixedDelay = 5000)
    public void syncViewCountToDB() {
        Set<String> keys = redisTemplate.keys(RedisPrefixType.POST_VIEW_COUNT + "*");
        if (keys == null || keys.isEmpty()) {
            return;
        }
        keys.forEach(postMetaService::updatePostViewCount);
    }

    @Scheduled(fixedDelay = 5000)
    public void syncCommentCountToDB() {
        Set<String> commentKeys = redisTemplate.keys(RedisPrefixType.POST_COMMENT_COUNT + "*");
        if (commentKeys == null || commentKeys.isEmpty()) {
            return;
        }
        commentKeys.forEach(postMetaService::updatePostCommentCount);
    }
}
