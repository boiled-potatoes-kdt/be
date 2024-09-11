package com.dain_review.domain.post.service;

import com.dain_review.domain.Image.service.ImageFileService;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.model.entity.PostMeta;
import com.dain_review.domain.post.model.request.PostRequest;
import com.dain_review.domain.post.model.request.PostSearchRequest;
import com.dain_review.domain.post.model.response.PostResponse;
import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.dain_review.domain.post.model.response.PostResponse.responseWithContentPreview;

@RequiredArgsConstructor
public abstract class AbstractPostService {

    protected final PostRepository postRepository;
    protected final UserRepository userRepository;
    protected final ImageFileService imageFileService;

    @Transactional
    public PostResponse createPost(Long userId, PostRequest postRequest, List<MultipartFile> imageFiles) {
        User user = userRepository.getUserById(userId);
        System.out.println("category type: " + postRequest.categoryType());
        Post post = createPostByCategoryType(postRequest, user);
        PostMeta postMeta = PostMeta.builder().post(post).viewCount(0L).commentCount(0L).build();

        post.setPostMeta(postMeta);
        postRepository.save(post);
        System.out.println("category type: " + post.getCategoryType().getDisplayName());

        // 이미지 저장 및 url 반환
        List<String> imageUrls = saveImages(imageFiles, post);

        String userImageUrl = imageFileService.getUserProfileUrl(post.getUser().getProfileImage());

        return PostResponse.responseWithoutContentPreview(post, userImageUrl, imageUrls, null, null);
    }

    @Transactional
    public PostResponse updatePost(Long userId, Long postId, PostRequest postRequest, List<MultipartFile> imageFiles) {
        Post post = postRepository.getPostByIdAndDeletedFalse(postId);
        post.updateBy(userId, postRequest);

        // 새로 추가된 이미지 저장
        List<String> imageUrls = updateImages(imageFiles, post, postRequest.deletedAttachedFiles());

        String userImageUrl = imageFileService.getUserProfileUrl(post.getUser().getProfileImage());

        return PostResponse.responseWithoutContentPreview(post, userImageUrl, imageUrls, null, null);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.getPostByIdAndDeletedFalse(postId);
        post.deleteBy(userId);
        postRepository.save(post);
    }

    @Transactional
    public abstract PostResponse getPost(Long userId, Long postId, PostSearchRequest postSearchRequest);

    @Transactional(readOnly = true)
    public abstract PagedResponse<PostResponse> getAllPosts(Long userId, Pageable pageable);

    @Transactional(readOnly = true)
    public abstract PagedResponse<PostResponse> searchPosts(Long userId, PostSearchRequest request, Pageable pageable);

    protected PagedResponse<PostResponse> mapPostsToPagedResponse(Page<Post> postsPage) {
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

    protected abstract List<String> saveImages(List<MultipartFile> imageFiles, Post post);
    protected abstract List<String> updateImages(List<MultipartFile> imageFiles, Post post, List<String> deletedImageFiles);

    protected abstract Post createPostByCategoryType(PostRequest postRequest, User user);

}
