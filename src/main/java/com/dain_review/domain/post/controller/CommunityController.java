package com.dain_review.domain.post.controller;


import com.dain_review.domain.post.model.entity.enums.CommunityType;
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
@RequestMapping("/api/post/communities")
public class CommunityController {

    private final PostService postService;
    private final String S3_PATH_PREFIX = S3PathPrefixType.S3_COMMUNITY_PATH.toString();

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @PostMapping // 커뮤니티 게시글 생성
    public ResponseEntity<?> createPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart("data") PostRequest postRequest,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles) {

        PostResponse communityResponse =
                postService.createPost(
                        S3_PATH_PREFIX, customUserDetails.getUserId(), postRequest, imageFiles);
        return API.OK(communityResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping("/{postId}") // 커뮤니티 게시글 단건 조회
    public ResponseEntity<?> getPost(@PathVariable Long postId) {

        PostResponse communityResponse = postService.getPost(S3_PATH_PREFIX, postId);
        return API.OK(communityResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @PatchMapping("/{postId}") // 커뮤니티 게시글 수정
    public ResponseEntity<?> updatePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @RequestPart("data") PostRequest postRequest,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFiles) {

        PostResponse communityResponse =
                postService.updatePost(
                        S3_PATH_PREFIX,
                        customUserDetails.getUserId(),
                        postId,
                        postRequest,
                        imageFiles);
        return API.OK(communityResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @DeleteMapping("/{postId}") // 커뮤니티 게시글 삭제
    public ResponseEntity<?> deletePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId) {

        postService.deletePost(customUserDetails.getUserId(), postId);
        return API.OK("게시글이 삭제 완료 되었습니다.");
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping
    public ResponseEntity<?> getAllPosts( // 커뮤니티 게시글 전체 목록 조회
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<PostResponse> communities =
                postService.getPostsByRole(customUserDetails.getUserId(), page, size);
        return API.OK(communities);
    }

    @PreAuthorize("hasAnyRole('ROLE_INFLUENCER', 'ROLE_ENTERPRISER')")
    @GetMapping("/type/{communityType}")
    public ResponseEntity<?> getPostsByCommunityType( // 커뮤니티 게시글 카테고리 별 목록 조회
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable CommunityType communityType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<PostResponse> communities =
                postService.getPostsByCommunityType(
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

        PagedResponse<PostResponse> communities =
                postService.searchPostsByRole(customUserDetails.getUserId(), keyword, page, size);
        return API.OK(communities);
    }
}
