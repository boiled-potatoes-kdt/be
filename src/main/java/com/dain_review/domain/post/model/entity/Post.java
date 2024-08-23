package com.dain_review.domain.post.model.entity;


import com.dain_review.domain.comment.model.entity.Comment;
import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.CommunityType;
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
import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    private String title;
    private String content;
    private String imageUrl;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @Enumerated(EnumType.STRING)
    private CommunityType communityType;

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

    public void setTitle(String title) {}

    public void setContent(String content) {}

    public void setCommunityType(CommunityType communityType) {}

    public void setUpdatedAt(LocalDateTime now) {}

    public void setImageUrl(String newImageUrl) {}
}
