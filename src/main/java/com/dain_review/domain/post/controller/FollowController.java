package com.dain_review.domain.post.controller;

import com.dain_review.domain.post.model.entity.enums.FollowType;
import com.dain_review.domain.post.model.request.FollowRequest;
import com.dain_review.domain.post.model.response.FollowResponse;
import com.dain_review.domain.post.service.FollowService;
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
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;

    // 생성
    @PostMapping
    public ResponseEntity<?> createPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart("data") FollowRequest followRequest,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles
    ) {
        FollowResponse followResponse = followService.createPost(
                customUserDetails.getUserId(), followRequest, imageFiles);
        return API.OK(followResponse);
    }

    // 단건조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId
    ) {
        FollowResponse followResponse =
                followService.getPost(customUserDetails.getUserId(), postId);
        return API.OK(followResponse);
    }

    // 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @RequestPart("data") FollowRequest followRequest,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles
    ) {
        FollowResponse followResponse = followService.updatePost(
                        customUserDetails.getUserId(), postId, followRequest, imageFiles);
        return API.OK(followResponse);
    }

    // 삭제(물리적 삭제 x)
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId
    ) {
        followService.deletePost(customUserDetails.getUserId(), postId);
        return API.OK();
    }

    // 목록조회
    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<FollowResponse> follows =
                followService.getAllPosts(customUserDetails.getUserId(), page, size);
        return API.OK(follows);
    }

    @GetMapping("type/{followType}")
    public ResponseEntity<?> getPostsByFollowType(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable FollowType followType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<FollowResponse> follows =
                followService.getPostsByFollowType(
                        customUserDetails.getUserId(), followType, page, size);
        return API.OK(follows);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<FollowResponse> follows =
                followService.searchPosts(customUserDetails.getUserId(), keyword, page, size);
        return API.OK(follows);
    }
}
