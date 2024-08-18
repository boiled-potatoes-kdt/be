package com.dain_review.domain.post.repository;


import com.dain_review.domain.post.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update PostMeta pm set pm.viewCount = pm.viewCount + 1 where pm.post = :post")
    void increaseViewCount(final Post post);
}
