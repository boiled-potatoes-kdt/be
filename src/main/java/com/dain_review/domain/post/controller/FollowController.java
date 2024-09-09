package com.dain_review.domain.post.controller;


import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.FollowType;
import com.dain_review.domain.post.model.request.PostRequest;
import com.dain_review.domain.post.model.response.PostResponse;
import com.dain_review.domain.post.service.PostService;
import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.global.api.API;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.type.S3PathPrefixType;
import java.util.List;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/follows")
public class FollowController {

    private final PostService postService;
    // 생성
    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @PostMapping
    public ResponseEntity<?> createPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart("data") PostRequest postRequest,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles) {
        PostResponse postResponse =
                postService.createPost(
                        S3PathPrefixType.S3_FOLLOW_PATH,
                        customUserDetails.getUserId(),
                        postRequest,
                        imageFiles);
        return API.OK(postResponse);
    }

    // 단건조회
    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        PostResponse postResponse = postService.getPost(S3PathPrefixType.S3_FOLLOW_PATH, postId);
        return API.OK(postResponse);
    }

    // 수정
    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @PatchMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @RequestPart("data") PostRequest postRequest,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles) {
        PostResponse postResponse =
                postService.updatePost(
                        S3PathPrefixType.S3_FOLLOW_PATH,
                        customUserDetails.getUserId(),
                        postId,
                        postRequest,
                        imageFiles);
        return API.OK(postResponse);
    }

    // 삭제(물리적 삭제 x)
    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId) {
        postService.deletePost(customUserDetails.getUserId(), postId);
        return API.OK("게시글이 삭제 완료 되었습니다.");
    }

    // 목록조회
    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<PostResponse> follows =
                postService.getAllPosts(page, size, CategoryType.FOLLOW);
        return API.OK(follows);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping("/type/{followType}")
    public ResponseEntity<?> getPostsByFollowType(
            @PathVariable FollowType followType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<PostResponse> follows =
                postService.getPostsByFollowType(followType, page, size);
        return API.OK(follows);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<PostResponse> follows =
                postService.searchPosts(CategoryType.FOLLOW, keyword, page, size);
        return API.OK(follows);
    }
}
