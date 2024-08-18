package com.dain_review.domain.post.model.entity;


import com.dain_review.domain.comment.model.entity.Comment;
import com.dain_review.domain.post.model.type.CategoryType;
import com.dain_review.domain.post.model.type.CommunityType;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.CascadeType;
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

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Post extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType; // 이제 CategoryType을 직접 사용

    private String title;
    private String content;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @Enumerated(EnumType.STRING)
    private CommunityType communityType; // 커뮤니티 속성

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private PostMeta postMeta; // 조회수, 댓글수

    public void setPostMeta(PostMeta postMeta) {
        this.postMeta = postMeta;
        postMeta.setPost(this);
    }
}
