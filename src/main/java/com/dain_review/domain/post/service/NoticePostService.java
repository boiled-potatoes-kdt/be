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
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NoticePostService extends AbstractPostService {

    private final ApplicationEventPublisher eventPublisher;

    public NoticePostService(
            PostRepository postRepository,
            UserRepository userRepository,
            ImageFileService imageFileService,
            ApplicationEventPublisher eventPublisher) {
        super(postRepository, userRepository, imageFileService);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public PostResponse getPost(Long userId, Long postId, PostSearchRequest postSearchRequest) {
        Post post = postRepository.getPostByIdAndDeletedFalse(postId);
        Long prevPosId =
                postRepository.findPreviousPost(
                        postId, null, CategoryType.NOTICE, null, null, postSearchRequest.keyword());
        Long nextPostId =
                postRepository.findNextPost(
                        postId, null, CategoryType.NOTICE, null, null, postSearchRequest.keyword());

        // 게시물의 모든 이미지 url 리스트를 반환
        List<String> imageUrls = imageFileService.findImageUrls(post.getId(), ContentType.POST);

        // 조회 이벤트 발생 시, 이미 조회된 Post 객체를 전달
        eventPublisher.publishEvent(new PostReadEvent(post));

        String userImageUrl = post.getUser().getProfileImageUrl();
        return PostResponse.responseWithoutContentPreview(
                post, userImageUrl, imageUrls, prevPosId, nextPostId);
    }

    @Override
    public PagedResponse<PostResponse> getAllPosts(Long userId, Pageable pageable) {
        Page<Post> postsPage = postRepository.findByCategoryType(CategoryType.NOTICE, pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    @Override
    public PagedResponse<PostResponse> searchPosts(
            Long userId, PostSearchRequest request, Pageable pageable) {
        Page<Post> postsPage =
                postRepository.searchByKeyword(CategoryType.NOTICE, request.keyword(), pageable);
        return mapPostsToPagedResponse(postsPage);
    }

    @Override
    protected List<String> saveImages(List<MultipartFile> imageFiles, Post post) {
        imageFileService.saveImageFiles(
                imageFiles, ContentType.POST, post.getId(), S3PathPrefixType.S3_NOTICE_PATH);
        return imageFileService.findImageUrls(post.getId(), ContentType.POST);
    }

    @Override
    protected List<String> updateImages(
            List<MultipartFile> imageFiles, Post post, List<String> deletedImageFiles) {
        imageFileService.saveImageFiles(
                imageFiles, ContentType.POST, post.getId(), S3PathPrefixType.S3_NOTICE_PATH);
        imageFileService.deleteImageFiles(deletedImageFiles, S3PathPrefixType.S3_NOTICE_PATH);
        return imageFileService.findImageUrls(post.getId(), ContentType.POST);
    }

    @Override
    protected Post createPostByCategoryType(PostRequest postRequest, User user) {
        return Post.createNoticePost(postRequest, user);
    }
}
