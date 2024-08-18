package com.dain_review.domain.post.service;


import com.dain_review.domain.post.exception.PostException;
import com.dain_review.domain.post.exception.errorcode.PostErrorCode;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.model.entity.PostMeta;
import com.dain_review.domain.post.model.request.CommunityRequest;
import com.dain_review.domain.post.model.response.CommunityResponse;
import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.global.model.PagedResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityService {

    private final PostRepository postRepository;

    public CommunityResponse createPost(CommunityRequest communityRequest) {
        Post post =
                Post.builder()
                        .categoryType(communityRequest.categoryType())
                        .title(communityRequest.title())
                        .content(communityRequest.content())
                        .communityType(communityRequest.communityType())
                        .build();

        PostMeta postMeta =
                PostMeta.builder()
                        .post(post)
                        .viewCount(0) // 조회수와 댓글수는 0으로 초기화
                        .commentCount(0)
                        .build();

        post.setPostMeta(postMeta);
        postRepository.save(post);

        return CommunityResponse.fromEntity(post);
    }

    public CommunityResponse getPost(Long postId) {
        Post post =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        return CommunityResponse.fromEntity(post);
    }

    public CommunityResponse updatePost(Long postId, CommunityRequest communityRequest) {
        Post existingPost =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        // 기존 PostMeta를 유지하기 위해 설정
        PostMeta existingPostMeta = existingPost.getPostMeta();

        Post updatedPost =
                Post.builder()
                        .id(existingPost.getId()) // 기존 ID 유지
                        .user(existingPost.getUser()) // 기존 User 유지
                        .categoryType(existingPost.getCategoryType()) // 기존 Category 유지
                        .title(communityRequest.title()) // 새 제목 설정
                        .content(communityRequest.content()) // 새 내용 설정
                        .communityType(communityRequest.communityType())
                        .createdAt(existingPost.getCreatedAt()) // 기존 생성일 유지
                        .updatedAt(LocalDateTime.now()) // 업데이트 시각 설정
                        .postMeta(existingPostMeta) // 기존 PostMeta 유지
                        .build();

        postRepository.save(updatedPost);

        return CommunityResponse.fromEntity(updatedPost);
    }

    public void deletePost(Long postId) {
        Post post =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        postRepository.delete(post);
    }

    public PagedResponse<CommunityResponse> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = postRepository.findAll(pageable);

        List<CommunityResponse> communities =
                postsPage.stream().map(CommunityResponse::fromEntity).collect(Collectors.toList());

        return new PagedResponse<>(
                communities,
                postsPage.getNumber(),
                postsPage.getSize(),
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.isLast());
    }
}
