package com.dain_review.domain.post.event;


import com.dain_review.domain.post.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PostReadEventListener {

    private final CommunityService communityService;

    @Async("asyncExecutor")
    @Transactional
    @EventListener
    public void onReceive(PostReadEvent event) {
        communityService.increaseViewCount(event.getPostId());
    }
}
