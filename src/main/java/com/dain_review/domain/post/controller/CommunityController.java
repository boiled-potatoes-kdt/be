package com.dain_review.domain.post.controller;


import com.dain_review.domain.post.model.entity.enums.CommunityType;
import com.dain_review.domain.post.model.request.CommunityRequest;
import com.dain_review.domain.post.model.response.CommunityResponse;
import com.dain_review.domain.post.service.CommunityService;
import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.global.api.API;
import com.dain_review.global.model.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityService communityService;

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @PostMapping // 커뮤니티 게시글 생성
    public ResponseEntity<?> createPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart("data") CommunityRequest communityRequest,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles) {

        CommunityResponse communityResponse =
                communityService.createPost(
                        customUserDetails.getUserId(), communityRequest, imageFiles);
        return API.OK(communityResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping("/{postId}") // 커뮤니티 게시글 단건 조회
    public ResponseEntity<?> getPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId) {

        CommunityResponse communityResponse =
                communityService.getPost(customUserDetails.getUserId(), postId);
        return API.OK(communityResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @PatchMapping("/{postId}") // 커뮤니티 게시글 수정
    public ResponseEntity<?> updatePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @RequestPart("data") CommunityRequest communityRequest,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles) {

        CommunityResponse communityResponse =
                communityService.updatePost(
                        customUserDetails.getUserId(), postId, communityRequest, imageFiles);
        return API.OK(communityResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @DeleteMapping("/{postId}") // 커뮤니티 게시글 삭제
    public ResponseEntity<?> deletePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId) {

        communityService.deletePost(customUserDetails.getUserId(), postId);
        return API.OK();
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping
    public ResponseEntity<?> getAllPosts( // 커뮤니티 게시글 전체 목록 조회
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<CommunityResponse> communities =
                communityService.getAllPosts(customUserDetails.getUserId(), page, size);
        return API.OK(communities);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping("type/{communityType}")
    public ResponseEntity<?> getPostsByCommunityType( // 커뮤니티 게시글 카테고리 별 목록 조회
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable CommunityType communityType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<CommunityResponse> communities =
                communityService.getPostsByCommunityType(
                        customUserDetails.getUserId(), communityType, page, size);
        return API.OK(communities);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping("/search")
    public ResponseEntity<?> searchPosts( // 커뮤니티 게시글 키워드 검색
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<CommunityResponse> communities =
                communityService.searchPosts(customUserDetails.getUserId(), keyword, page, size);
        return API.OK(communities);
    }
}
