package com.dain_review.domain.post.model.entity;


import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PostMeta extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private Integer viewCount;
    private Integer commentCount;

    public void setPost(Post post) {
        this.post = post;
    }
}
