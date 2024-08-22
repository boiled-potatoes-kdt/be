package com.dain_review.domain.post.controller;


import com.dain_review.domain.post.model.entity.enums.CommunityType;
import com.dain_review.domain.post.model.request.CommunityRequest;
import com.dain_review.domain.post.model.response.CommunityResponse;
import com.dain_review.domain.post.service.CommunityService;
import com.dain_review.global.api.API;
import com.dain_review.global.model.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping // 커뮤니티 게시글 생성
    public ResponseEntity<?> createPost(@RequestBody CommunityRequest communityRequest) {
        CommunityResponse communityResponse = communityService.createPost(communityRequest);
        return API.OK(communityResponse);
    }

    @GetMapping("/{postId}") // 커뮤니티 게시글 단건 조회
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        CommunityResponse communityResponse = communityService.getPost(postId);
        return API.OK(communityResponse);
    }

    @PatchMapping("/{postId}") // 커뮤니티 게시글 수정
    public ResponseEntity<?> updatePost(
            @PathVariable Long postId, @RequestBody CommunityRequest communityRequest) {
        CommunityResponse communityResponse = communityService.updatePost(postId, communityRequest);
        return API.OK(communityResponse);
    }

    @DeleteMapping("/{postId}") // 커뮤니티 게시글 삭제
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        communityService.deletePost(postId);
        return API.OK();
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts( // 커뮤니티 게시글 전체 목록 조회
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<CommunityResponse> communities = communityService.getAllPosts(page, size);
        return API.OK(communities);
    }

    @GetMapping("type/{communityType}")
    public ResponseEntity<?> getPostsByCommunityType( // 커뮤니티 게시글 중 카테고리 별 목록 조회
            @PathVariable CommunityType communityType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<CommunityResponse> communities =
                communityService.getPostsByCommunityType(communityType, page, size);
        return API.OK(communities);
    }
}
