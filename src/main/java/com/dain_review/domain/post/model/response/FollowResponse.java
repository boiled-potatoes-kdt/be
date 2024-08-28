package com.dain_review.domain.post.model.response;

import com.dain_review.domain.post.model.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FollowResponse {

    private Long id;
    private String title;
    private String content;
    private List<String> imageUrls;
    private String nickName; // 작성자 닉네임
    private String category;
    private String followType;
    private LocalDateTime createdAt;
    private Long viewCount;
    private Long commentCount;
    private String contentPreview; // 글 내용 미리보기

    // 게시글 미리보기가 포함된 응답 (이미지 url 제외)
    public static FollowResponse responseWithContentPreview(Post post) {
        return FollowResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .nickName(post.getUser().getNickname())
                .category(post.getCategoryType().getDisplayName())
                .followType(post.getFollowType().getDisplayName())
                .createdAt(post.getCreatedAt())
                .viewCount(post.getPostMeta().getViewCount())
                .commentCount(post.getPostMeta().getCommentCount())
                .contentPreview(getContentPreview(post.getContent()))
                .build();
    }

    // 게시글 미리보기가 포함되지 않은 응답 (이미지 url 포함)
    public static FollowResponse responseWithoutContentPreview(Post post, List<String> imageUrls) {
        return FollowResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrls(imageUrls)
                .nickName(post.getUser().getNickname())
                .category(post.getCategoryType().getDisplayName())
                .followType(post.getFollowType().getDisplayName())
                .createdAt(post.getCreatedAt())
                .viewCount(post.getPostMeta().getViewCount())
                .commentCount(post.getPostMeta().getCommentCount())
                .build();
    }

    // 글 내용을 HTML로 받았을 때 HTML 태그는 무시하고 미리보기 생성
    private static String getContentPreview(String content) {
        int previewLength = 100; // 미리보기 길이 설정 (예시: 100자)
        String plainTextContent = content.replaceAll("<[^>]*>", ""); // HTML 태그 제거
        return plainTextContent.length() > previewLength
                ? plainTextContent.substring(0, previewLength) + "..."
                : plainTextContent;
    }
}
