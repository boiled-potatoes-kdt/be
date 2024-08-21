package com.dain_review.domain.post.event;


import com.dain_review.domain.post.service.PostMetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostReadEventListener {

    private final PostMetaService postMetaService;

    @Async("asyncExecutor")
    @Transactional
    @EventListener
    public void onReceive(PostReadEvent event) {
        log.info("이벤트 수신 완료 postId: {}", event.getPost().getId());
        postMetaService.incrementViewCount(event.getPost().getId());
        log.info("이벤트 처리 완료 postId: {}", event.getPost().getId());
    }
}
