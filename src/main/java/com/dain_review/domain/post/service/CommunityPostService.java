package com.dain_review.domain.post.service;

import com.dain_review.domain.Image.entity.enums.ContentType;
import com.dain_review.domain.Image.service.ImageFileService;
import com.dain_review.domain.post.event.PostReadEvent;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.request.PostRequest;
import com.dain_review.domain.post.model.request.PostSearchRequest;
import com.dain_review.domain.post.model.response.PostResponse;
import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.type.S3PathPrefixType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CommunityPostService extends AbstractPostService {

    private final ApplicationEventPublisher eventPublisher;

    public CommunityPostService(PostRepository postRepository, UserRepository userRepository, ImageFileService imageFileService, ApplicationEventPublisher eventPublisher) {
        super(postRepository, userRepository, imageFileService);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public PostResponse getPost(Long userId, Long postId, PostSearchRequest postSearchRequest) {
        User user = userRepository.getUserById(userId);
        Post post = postRepository.getPostByIdAndDeletedFalse(postId);
        String userImageUrl = imageFileService.getUserProfileUrl(post.getUser().getProfileImage());
        Long prevPosId = postRepository.findPreviousPost(postId, user.getRole(), CategoryType.COMMUNITY, postSearchRequest.communityType(), null, postSearchRequest.keyword());
        Long nextPostId = postRepository.findNextPost(postId, user.getRole(),  CategoryType.COMMUNITY, postSearchRequest.communityType(), null, postSearchRequest.keyword());

        // 게시물의 모든 이미지 url 리스트를 반환
        List<String> imageUrls = imageFileService.findImageUrls(post.getId(), ContentType.POST, S3PathPrefixType.S3_COMMUNITY_PATH);

        // 조회 이벤트 발생 시, 이미 조회된 Post 객체를 전달
        eventPublisher.publishEvent(new PostReadEvent(post));

        return PostResponse.responseWithoutContentPreview(post, userImageUrl, imageUrls, prevPosId, nextPostId);
    }

    @Override
    public PagedResponse<PostResponse> getAllPosts(Long userId, Pageable pageable) {
        User user = userRepository.getUserById(userId);
        Page<Post> postsPage = postRepository.findCommunityPostsByRole(user.getRole(), pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    @Override
    public PagedResponse<PostResponse> searchPosts(Long userId, PostSearchRequest request, Pageable pageable) {
        User user = userRepository.getUserById(userId);

        Page<Post> posts = postRepository.searchCommunityPost(user.getRole(), request.keyword(), request.communityType(), pageable);
        return mapPostsToPagedResponse(posts);
    }

    @Override
    protected List<String> saveImages(List<MultipartFile> imageFiles, Post post) {
        imageFileService.saveImageFiles(imageFiles, ContentType.POST, post.getId(), S3PathPrefixType.S3_COMMUNITY_PATH);
        return imageFileService.findImageUrls(post.getId(), ContentType.POST, S3PathPrefixType.S3_COMMUNITY_PATH);
    }

    @Override
    protected List<String> updateImages(List<MultipartFile> imageFiles, Post post, List<String> deletedImageFiles) {
        imageFileService.saveImageFiles(imageFiles, ContentType.POST, post.getId(), S3PathPrefixType.S3_COMMUNITY_PATH);
        imageFileService.deleteImageFiles(deletedImageFiles, S3PathPrefixType.S3_COMMUNITY_PATH);
        return imageFileService.findImageUrls(post.getId(), ContentType.POST, S3PathPrefixType.S3_COMMUNITY_PATH);
    }

    @Override
    protected Post createPostByCategoryType(PostRequest postRequest, User user) {
        return Post.createCommunityPost(postRequest, user);
    }
}
