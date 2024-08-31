package com.dain_review.domain.post.service;

import static com.dain_review.global.util.ImageFileValidUtil.isValidImageFile;

import com.dain_review.domain.post.event.PostReadEvent;
import com.dain_review.domain.post.exception.PostException;
import com.dain_review.domain.post.exception.errortype.PostErrorCode;
import com.dain_review.domain.post.model.entity.AttachedFile;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.model.entity.PostMeta;
import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.CommunityType;
import com.dain_review.domain.post.model.entity.enums.FollowType;
import com.dain_review.domain.post.model.request.PostRequest;
import com.dain_review.domain.post.model.response.PostResponse;
import com.dain_review.domain.post.repository.AttachedFileRepository;
import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.exception.errortype.UserErrorCode;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.util.S3Util;
import com.dain_review.global.util.error.S3Exception;
import com.dain_review.global.util.errortype.S3ErrorCode;
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
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AttachedFileRepository attachedFileRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final S3Util s3Util;

    // 목록조회, 삭제 제외하고 이미지 조회 필요함
    public PostResponse createPost(
            String S3_PATH_PREFIX,
            Long userId,
            PostRequest postRequest,
            List<MultipartFile> imageFiles) {
        User user = getUser(userId);
        Post post = determineCreatePostCategoryType(postRequest, user);
        PostMeta postMeta =
                PostMeta.builder()
                        .post(post)
                        .viewCount(0L) // 조회수와 댓글수는 0으로 초기화
                        .commentCount(0L)
                        .build();

        post.setPostMeta(postMeta);
        postRepository.save(post);

        // 이미지 저장 및 url 반환
        saveImageFiles(imageFiles, post, S3_PATH_PREFIX);
        List<String> imageUrls = findImageUrls(post.getId(), S3_PATH_PREFIX);

        return PostResponse.responseWithoutContentPreview(post, imageUrls);
    }

    public PostResponse getPost(String S3_PATH_PREFIX, Long postId) {
        Post post = postRepository.findByIdAndDeletedFalse(postId);
        if (post == null) {
            throw new PostException(PostErrorCode.POST_NOT_FOUND);
        }

        // 게시물의 모든 이미지 url 리스트를 반환
        List<String> imageUrls = findImageUrls(post.getId(), S3_PATH_PREFIX);

        // 조회 이벤트 발생 시, 이미 조회된 Post 객체를 전달
        eventPublisher.publishEvent(new PostReadEvent(post));

        return PostResponse.responseWithoutContentPreview(post, imageUrls);
    }

    public PostResponse updatePost(
            String S3_PATH_PREFIX,
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
        saveImageFiles(imageFiles, existingPost, S3_PATH_PREFIX);
        deleteImageFiles(postRequest.deletedAttachedFiles(), S3_PATH_PREFIX);
        List<String> imageUrls = findImageUrls(existingPost.getId(), S3_PATH_PREFIX);

        return PostResponse.responseWithoutContentPreview(existingPost, imageUrls);
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
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = postRepository.findByCategoryType(categoryType, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    public PagedResponse<PostResponse> getPostsByCommunityType(
            CommunityType communityType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage =
                postRepository.findByCategoryTypeAndCommunityType(
                        CategoryType.COMMUNITY, communityType, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    public PagedResponse<PostResponse> getPostsByFollowType(
            FollowType followType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage =
                postRepository.findByCategoryTypeAndFollowType(
                        CategoryType.FOLLOW, followType, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    public PagedResponse<PostResponse> searchPosts(
            CategoryType categoryType, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = postRepository.searchByKeyword(categoryType, keyword, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    private PagedResponse<PostResponse> mapPostsToPagedResponse(Page<Post> postsPage) {
        List<PostResponse> communities =
                postsPage.stream()
                        .map(PostResponse::responseWithContentPreview)
                        .collect(Collectors.toList());

        return new PagedResponse<>(
                communities, postsPage.getTotalElements(), postsPage.getTotalPages());
    }

    private User getUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    private void saveImageFiles(List<MultipartFile> imageFiles, Post post, String S3_PATH_PREFIX) {
        if (imageFiles == null || imageFiles.isEmpty()) {
            return;
        }

        for (MultipartFile imageFile : imageFiles) {
            String fileName = null;

            if (imageFile != null && !imageFile.isEmpty()) {
                if (!isValidImageFile(imageFile)) {
                    throw new S3Exception(S3ErrorCode.INVALID_IMAGE_FILE);
                }

                fileName = s3Util.saveImage(imageFile, S3_PATH_PREFIX).join();
                // attached_file 테이블에 레코드 추가
                AttachedFile file = AttachedFile.builder().post(post).fileName(fileName).build();

                attachedFileRepository.save(file);
            }
        }
    }

    private void deleteImageFiles(List<String> fileNames, String S3_PATH_PREFIX) {
        for (String fileName : fileNames) {
            s3Util.deleteImage(fileName, S3_PATH_PREFIX);
            AttachedFile file = attachedFileRepository.findByFileName(fileName);
            attachedFileRepository.delete(file);
        }
    }

    private List<String> findImageUrls(Long postId, String S3_PATH_PREFIX) {
        List<AttachedFile> attachedFiles = attachedFileRepository.findByPostId(postId);
        return attachedFiles.stream()
                .map(
                        it -> {
                            return (it != null)
                                    ? s3Util.selectImage(it.getFileName(), S3_PATH_PREFIX)
                                    : null;
                        })
                .toList();
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
