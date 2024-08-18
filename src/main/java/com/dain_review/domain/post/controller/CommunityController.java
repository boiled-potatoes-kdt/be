package com.dain_review.domain.post.controller;


import com.dain_review.domain.post.model.request.CommunityRequest;
import com.dain_review.domain.post.model.response.CommunityResponse;
import com.dain_review.domain.post.service.CommunityService;
import com.dain_review.global.model.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping // 커뮤니티 게시글 생성
    public ResponseEntity<CommunityResponse> createPost(
            @RequestBody CommunityRequest communityRequest) {
        CommunityResponse communityResponse = communityService.createPost(communityRequest);
        return ResponseEntity.ok(communityResponse);
    }

    @GetMapping("/{postId}") // 커뮤니티 게시글 단건 조회
    public ResponseEntity<CommunityResponse> getPost(@PathVariable Long postId) {
        CommunityResponse communityResponse = communityService.getPost(postId);
        return ResponseEntity.ok(communityResponse);
    }

    @PutMapping("/{postId}") // 커뮤니티 게시글 수정
    public ResponseEntity<CommunityResponse> updatePost(
            @PathVariable Long postId, @RequestBody CommunityRequest communityRequest) {
        CommunityResponse communityResponse = communityService.updatePost(postId, communityRequest);
        return ResponseEntity.ok(communityResponse);
    }

    @DeleteMapping("/{postId}") // 커뮤니티 게시글 삭제
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        communityService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagedResponse<CommunityResponse>> getAllPosts( // 커뮤니티 게시글 전체 목록 조회
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<CommunityResponse> communities = communityService.getAllPosts(page, size);
        return ResponseEntity.ok(communities);
    }
}
