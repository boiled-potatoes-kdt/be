package com.dain_review.domain.post.model.response;


import com.dain_review.domain.post.model.entity.Post;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommunityResponse {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String userName; // todo : 추후에 닉네임으로 변경
    private String category;
    private String communityType;
    private LocalDateTime createdAt;
    private int viewCount;
    private int commentCount;
    private String contentPreview; // 글 내용 미리보기

    // 게시글 미리보기가 포함된 응답
    public static CommunityResponse responseWithContentPreview(Post post) {
        return CommunityResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .userName(post.getUser().getName())
                .category(post.getCategoryType().getDisplayName())
                .communityType(post.getCommunityType().getDisplayName())
                .createdAt(post.getCreatedAt())
                .viewCount(post.getPostMeta().getViewCount())
                .commentCount(post.getPostMeta().getCommentCount())
                .contentPreview(getContentPreview(post.getContent()))
                .build();
    }

    // 게시글 미리보기가 포함되지 않은 응답
    public static CommunityResponse responseWithoutContentPreview(Post post) {
        return CommunityResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .userName(post.getUser().getName())
                .category(post.getCategoryType().getDisplayName())
                .communityType(post.getCommunityType().getDisplayName())
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
