package com.dain_review.domain.post.service;

import com.dain_review.domain.Image.entity.enums.ContentType;
import com.dain_review.domain.Image.service.ImageFileService;
import com.dain_review.domain.post.event.PostReadEvent;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.FollowType;
import com.dain_review.domain.post.model.request.PostSearchRequest;
import com.dain_review.domain.post.model.response.PostResponse;
import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.type.S3PathPrefixType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FollowPostService extends AbstractPostService{

    private final ApplicationEventPublisher eventPublisher;

    public FollowPostService(PostRepository postRepository, UserRepository userRepository, ImageFileService imageFileService, ApplicationEventPublisher eventPublisher) {
        super(postRepository, userRepository, imageFileService);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public PostResponse getPost(Long userId, Long postId, PostSearchRequest postSearchRequest) {
        Post post = postRepository.getPostByIdAndDeletedFalse(postId);
        Long prevPosId = postRepository.findPreviousPost(postId, null, CategoryType.FOLLOW, null, postSearchRequest.followType(), postSearchRequest.keyword());
        Long nextPostId = postRepository.findNextPost(postId, null, CategoryType.FOLLOW, null, postSearchRequest.followType(), postSearchRequest.keyword());

        // 게시물의 모든 이미지 url 리스트를 반환
        List<String> imageUrls = imageFileService.findImageUrls(post.getId(), ContentType.POST, S3PathPrefixType.S3_FOLLOW_PATH);

        // 조회 이벤트 발생 시, 이미 조회된 Post 객체를 전달
        eventPublisher.publishEvent(new PostReadEvent(post));

        String userImageUrl = imageFileService.getUserProfileUrl(post.getUser().getProfileImage());
        return PostResponse.responseWithoutContentPreview(post, userImageUrl, imageUrls, prevPosId, nextPostId);
    }

    @Override
    public PagedResponse<PostResponse> getAllPosts(Long userId, Pageable pageable) {
        Page<Post> postsPage = postRepository.findByCategoryType(CategoryType.FOLLOW, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    @Override
    public PagedResponse<PostResponse> searchPosts(Long userId, String keyword, Pageable pageable) {
        Page<Post> postsPage = postRepository.searchByKeyword(CategoryType.FOLLOW, keyword, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    public PagedResponse<PostResponse> getPostsByFollowType(FollowType followType, Pageable pageable) {
        Page<Post> postsPage = postRepository.findByCategoryTypeAndFollowType(
                        CategoryType.FOLLOW, followType, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    @Override
    protected List<String> saveImages(List<MultipartFile> imageFiles, Post post) {
        S3PathPrefixType s3Path = S3PathPrefixType.S3_FOLLOW_PATH;
        imageFileService.saveImageFiles(imageFiles, ContentType.POST, post.getId(), s3Path);
        return imageFileService.findImageUrls(post.getId(), ContentType.POST, s3Path);
    }

    @Override
    protected List<String> updateImages(List<MultipartFile> imageFiles, Post post, List<String> deletedImageFiles) {
        S3PathPrefixType s3Path = S3PathPrefixType.S3_FOLLOW_PATH;
        imageFileService.saveImageFiles(imageFiles, ContentType.POST, post.getId(), s3Path);
        imageFileService.deleteImageFiles(deletedImageFiles, s3Path);
        return imageFileService.findImageUrls(post.getId(), ContentType.POST, s3Path);
    }
}
