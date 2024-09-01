package com.dain_review.domain.post.controller;

import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.request.PostRequest;
import com.dain_review.domain.post.model.response.PostResponse;
import com.dain_review.domain.post.service.PostService;
import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.global.api.API;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.type.S3PathPrefixType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/post/notices")
public class NoticeController {

    private final PostService postService;
    private final String S3_PATH_PREFIX = S3PathPrefixType.S3_NOTICE_PATH.toString();

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart("data") PostRequest postRequest,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles
    ) {
        PostResponse postResponse = postService.createPost(
                S3_PATH_PREFIX, customUserDetails.getUserId(), postRequest, imageFiles);
        return API.OK(postResponse);
    }

    // 단건조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(
            @PathVariable Long postId
    ) {
        PostResponse postResponse =
                postService.getPost(S3_PATH_PREFIX, postId);
        return API.OK(postResponse);
    }

    // 수정
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PatchMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @RequestPart("data") PostRequest postRequest,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles
    ) {
        PostResponse postResponse = postService.updatePost(
                S3_PATH_PREFIX, customUserDetails.getUserId(), postId, postRequest, imageFiles);
        return API.OK(postResponse);
    }

    // 삭제(물리적 삭제 x)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId
    ) {
        postService.deletePost(customUserDetails.getUserId(), postId);
        return API.OK("게시글이 삭제 완료 되었습니다.");
    }

    // 목록조회
    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<PostResponse> follows =
                postService.getAllPosts(page, size, CategoryType.NOTICE);
        return API.OK(follows);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<PostResponse> follows =
                postService.searchPosts(CategoryType.NOTICE, keyword, page, size);
        return API.OK(follows);
    }
}
