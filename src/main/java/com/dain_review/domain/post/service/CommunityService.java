package com.dain_review.domain.post.service;

import static com.dain_review.global.util.ImageFileValidUtil.isValidImageFile;

import com.dain_review.domain.post.event.PostReadEvent;
import com.dain_review.domain.post.exception.PostException;
import com.dain_review.domain.post.exception.errorcode.PostErrorCode;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.model.entity.PostMeta;
import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.CommunityType;
import com.dain_review.domain.post.model.request.CommunityRequest;
import com.dain_review.domain.post.model.response.CommunityResponse;
import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.exception.errortype.UserErrorCode;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.util.S3Util;
import com.dain_review.global.util.error.S3Exception;
import com.dain_review.global.util.errortype.S3ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityService {

    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final S3Util s3Util;

    public CommunityResponse createPost(
            Long userId, CommunityRequest communityRequest, MultipartFile imageFile) {

        User user = getUser(userId);

        String fileName = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            if (!isValidImageFile(imageFile)) {
                throw new S3Exception(S3ErrorCode.INVALID_IMAGE_FILE);
            }
            fileName = s3Util.saveImage(imageFile).join();
        }

        Post post =
                Post.builder()
                        .user(user)
                        .categoryType(communityRequest.categoryType())
                        .title(communityRequest.title())
                        .content(communityRequest.content())
                        .communityType(communityRequest.communityType())
                        .imageUrl(fileName)
                        .build();

        PostMeta postMeta =
                PostMeta.builder()
                        .post(post)
                        .viewCount(0L) // 조회수와 댓글수는 0으로 초기화
                        .commentCount(0L)
                        .build();

        post.setPostMeta(postMeta);
        postRepository.save(post);

        return CommunityResponse.responseWithoutContentPreview(post);
    }

    public CommunityResponse getPost(Long userId, Long postId) {
        getUser(userId);
        Post post =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        // 조회 이벤트 발생 시, 이미 조회된 Post 객체를 전달
        eventPublisher.publishEvent(new PostReadEvent(post));

        return CommunityResponse.responseWithoutContentPreview(post);
    }

    public CommunityResponse updatePost(
            Long userId, Long postId, CommunityRequest communityRequest) {
        User user = getUser(userId);

        Post existingPost =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!existingPost.getUser().getId().equals(user.getId())) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ACCESS);
        }

        PostMeta existingPostMeta = existingPost.getPostMeta();

        Post updatedPost =
                Post.builder()
                        .id(existingPost.getId())
                        .user(existingPost.getUser())
                        .categoryType(existingPost.getCategoryType())
                        .title(communityRequest.title()) // 새 제목 설정
                        .content(communityRequest.content()) // 새 내용 설정
                        .communityType(communityRequest.communityType())
                        .createdAt(existingPost.getCreatedAt())
                        .updatedAt(LocalDateTime.now()) // 업데이트 시각 설정
                        .postMeta(existingPostMeta)
                        .build();

        postRepository.save(updatedPost);

        return CommunityResponse.responseWithoutContentPreview(updatedPost);
    }

    public void deletePost(Long userId, Long postId) {
        User user = getUser(userId);

        Post post =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ACCESS);
        }

        postRepository.delete(post);
    }

    public PagedResponse<CommunityResponse> getAllPosts(Long userId, int page, int size) {
        getUser(userId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = postRepository.findByCategoryType(CategoryType.COMMUNITY, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    public PagedResponse<CommunityResponse> getPostsByCommunityType(
            Long userId, CommunityType communityType, int page, int size) {
        getUser(userId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage =
                postRepository.findByCategoryTypeAndCommunityType(
                        CategoryType.COMMUNITY, communityType, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    public PagedResponse<CommunityResponse> searchPosts(
            Long userId, String keyword, int page, int size) {
        getUser(userId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage =
                postRepository.searchByKeyword(CategoryType.COMMUNITY, keyword, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    private PagedResponse<CommunityResponse> mapPostsToPagedResponse(Page<Post> postsPage) {
        List<CommunityResponse> communities =
                postsPage.stream()
                        .map(CommunityResponse::responseWithContentPreview)
                        .collect(Collectors.toList());

        return new PagedResponse<>(
                communities, postsPage.getTotalElements(), postsPage.getTotalPages());
    }

    private User getUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}
