package com.dain_review.domain.post.service;

import static com.dain_review.domain.post.model.response.PostResponse.responseWithContentPreview;

import com.dain_review.domain.Image.entity.enums.ContentType;
import com.dain_review.domain.Image.service.ImageFileService;
import com.dain_review.domain.post.event.PostReadEvent;
import com.dain_review.domain.post.exception.PostException;
import com.dain_review.domain.post.exception.errortype.PostErrorCode;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.model.entity.PostMeta;
import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.CommunityType;
import com.dain_review.domain.post.model.entity.enums.FollowType;
import com.dain_review.domain.post.model.request.PostRequest;
import com.dain_review.domain.post.model.response.PostResponse;
import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.exception.errortype.UserErrorCode;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.type.S3PathPrefixType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageFileService imageFileService;
    private final ApplicationEventPublisher eventPublisher;

    public PostResponse createPost(
            S3PathPrefixType s3PathPrefixType,
            Long userId,
            PostRequest postRequest,
            List<MultipartFile> imageFiles) {
        User user = getUser(userId);
        Post post = determineCreatePostCategoryType(postRequest, user);
        PostMeta postMeta = PostMeta.builder().post(post).viewCount(0L).commentCount(0L).build();

        post.setPostMeta(postMeta);
        postRepository.save(post);

        // 이미지 저장 및 url 반환
        imageFileService.saveImageFiles(imageFiles, ContentType.POST, post.getId(), s3PathPrefixType);
        List<String> imageUrls = imageFileService.findImageUrls(post.getId(), ContentType.POST, s3PathPrefixType);

        String userImageUrl = imageFileService.getUserProfileUrl(post.getUser().getProfileImage());
        return PostResponse.responseWithoutContentPreview(post, userImageUrl, imageUrls);
    }

    public PostResponse getPost(S3PathPrefixType s3PathPrefixType, Long postId) {
        Post post = postRepository.findByIdAndDeletedFalse(postId);
        if (post == null) {
            throw new PostException(PostErrorCode.POST_NOT_FOUND);
        }

        // 게시물의 모든 이미지 url 리스트를 반환
        List<String> imageUrls = imageFileService.findImageUrls(post.getId(), ContentType.POST, s3PathPrefixType);

        // 조회 이벤트 발생 시, 이미 조회된 Post 객체를 전달
        eventPublisher.publishEvent(new PostReadEvent(post));

        String userImageUrl = imageFileService.getUserProfileUrl(post.getUser().getProfileImage());
        return PostResponse.responseWithoutContentPreview(post, userImageUrl, imageUrls);
    }

    public PostResponse updatePost(
            S3PathPrefixType s3PathPrefixType,
            Long userId,
            Long postId,
            PostRequest postRequest,
            List<MultipartFile> imageFiles) {
        Post existingPost =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        existingPost.updateBy(userId, postRequest);

        // 새로 추가된 이미지 저장
        imageFileService.saveImageFiles(imageFiles, ContentType.POST, existingPost.getId(), s3PathPrefixType);
        imageFileService.deleteImageFiles(postRequest.deletedAttachedFiles(), s3PathPrefixType);
        List<String> imageUrls =
                imageFileService.findImageUrls(existingPost.getId(), ContentType.POST, s3PathPrefixType);

        String userImageUrl =
                imageFileService.getUserProfileUrl(existingPost.getUser().getProfileImage());
        return PostResponse.responseWithoutContentPreview(existingPost, userImageUrl, imageUrls);
    }

    public void deletePost(Long userId, Long postId) {
        Post post =
                postRepository
                        .findById(postId)
                        .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        post.deleteBy(userId);
        postRepository.save(post);
    }

    public PagedResponse<PostResponse> getAllPosts(int page, int size, CategoryType categoryType) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Post> postsPage = postRepository.findByCategoryType(categoryType, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    public PagedResponse<PostResponse> getPostsByRole(Long userId, int page, int size) {
        User user = getUser(userId);
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Post> postsPage = postRepository.findCommunityPostsByRole(user.getRole(), pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    public PagedResponse<PostResponse> getPostsByCommunityType(
            Long userId, CommunityType communityType, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        User user = getUser(userId);
        Page<Post> postsPage =
                postRepository.findByCategoryTypeAndCommunityType(
                        user.getRole(), CategoryType.COMMUNITY, communityType, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    public PagedResponse<PostResponse> getPostsByFollowType(
            FollowType followType, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Post> postsPage =
                postRepository.findByCategoryTypeAndFollowType(
                        CategoryType.FOLLOW, followType, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    public PagedResponse<PostResponse> searchPostsByRole(
            Long userId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        User user = getUser(userId);
        Page<Post> postsPage =
                postRepository.searchByKeywordAndRole(
                        user.getRole(), CategoryType.COMMUNITY, keyword, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    public PagedResponse<PostResponse> searchPosts(
            CategoryType categoryType, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Post> postsPage = postRepository.searchByKeyword(categoryType, keyword, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    private PagedResponse<PostResponse> mapPostsToPagedResponse(Page<Post> postsPage) {

        List<PostResponse> communities =
                postsPage.stream()
                        .map(
                                post -> {
                                    String userProfileImageName = post.getUser().getProfileImage();
                                    String profileImageUrl =
                                            imageFileService.getUserProfileUrl(
                                                    userProfileImageName);
                                    return responseWithContentPreview(post, profileImageUrl);
                                })
                        .collect(Collectors.toList());

        return new PagedResponse<>(
                communities, postsPage.getTotalElements(), postsPage.getTotalPages());
    }

    private User getUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    private Post determineCreatePostCategoryType(PostRequest postRequest, User user) {
        if (postRequest.categoryType().equals(CategoryType.COMMUNITY)) {
            return Post.createCommunityPost(postRequest, user);
        } else if (postRequest.categoryType().equals(CategoryType.FOLLOW)) {
            return Post.createFollowyPost(postRequest, user);
        }
        return Post.createNoticePost(postRequest, user);
    }
}
