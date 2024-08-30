package com.dain_review.domain.post.model.entity;


import com.dain_review.domain.comment.model.entity.Comment;
import com.dain_review.domain.post.exception.PostException;
import com.dain_review.domain.post.exception.errortype.PostErrorCode;
import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.CommunityType;
import com.dain_review.domain.post.model.entity.enums.FollowType;
import com.dain_review.domain.post.model.request.PostRequest;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Post extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    private String title;
    private String content;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<AttachedFile> attachedFile;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @Enumerated(EnumType.STRING)
    private CommunityType communityType;

    @Enumerated(EnumType.STRING)
    private FollowType followType;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private boolean deleted;

    @OneToOne(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private PostMeta postMeta;

    public void setPostMeta(PostMeta postMeta) {
        this.postMeta = postMeta;
        postMeta.setPost(this);
    }

    public void updateBy(Long userId, PostRequest request) {
        if(this.user.isNotSame(userId)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ACCESS);
        }
        this.title = request.title();
        this.content = request.content();
    }

    public void deleteBy(Long userId) {
        if(this.user.isNotSame(userId)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ACCESS);
        }
        this.deleted = true;
    }

    public static Post createCommunityPost(PostRequest postRequest, User user) {
        return Post.builder()
                .user(user)
                .categoryType(postRequest.categoryType())
                .title(postRequest.title())
                .content(postRequest.content())
                .communityType(postRequest.communityType())
                .build();
    }

    public static Post createFollowyPost(PostRequest postRequest, User user) {
        return Post.builder()
                .user(user)
                .categoryType(postRequest.categoryType())
                .title(postRequest.title())
                .content(postRequest.content())
                .followType(postRequest.followType())
                .build();
    }

    public static Post createNoticePost(PostRequest postRequest, User user) {
        return Post.builder()
                .user(user)
                .categoryType(postRequest.categoryType())
                .title(postRequest.title())
                .content(postRequest.content())
                .build();
    }
}
