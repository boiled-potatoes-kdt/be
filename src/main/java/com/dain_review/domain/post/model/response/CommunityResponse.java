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
    /*private String author;*/
    // 작성자 이름(닉네임 또는 업체명)-보류 개발 아직 안함
    private String category; // 카테고리 이름
    private String communityType;
    private LocalDateTime createdAt; // 게시글 등록일
    private int viewCount; // 조회수
    private int commentCount; // 댓글 수
    private String contentPreview; // 글 내용 미리보기

    public static CommunityResponse fromEntity(Post post) {
        return CommunityResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                /*.author(post.getUser().getUsername()) */
                .category(post.getCategoryType().getDisplayName())
                .communityType(post.getCommunityType().getDisplayName())
                .createdAt(post.getCreatedAt())
                .viewCount(post.getPostMeta().getViewCount())
                .commentCount(post.getPostMeta().getCommentCount())
                .contentPreview(getContentPreview(post.getContent()))
                .build();
    }

    private static String getContentPreview(String content) {
        int previewLength = 100; // 미리보기 길이 설정 (예시: 100자)
        return content.length() > previewLength
                ? content.substring(0, previewLength) + "..."
                : content;
    }
}
