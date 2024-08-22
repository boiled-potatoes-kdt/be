package com.dain_review.domain.post.repository;


import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.CommunityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 커뮤니티 전체 게시글 조회
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.postMeta WHERE p.categoryType = :categoryType")
    Page<Post> findByCategoryType(
            @Param("categoryType") CategoryType categoryType, Pageable pageable);

    // 게시글 중, 카테고리로 분류하여 목록 조회
    @Query(
            "SELECT p FROM Post p JOIN FETCH p.postMeta WHERE p.categoryType = :categoryType AND p.communityType = :communityType")
    Page<Post> findByCategoryTypeAndCommunityType(
            @Param("categoryType") CategoryType categoryType,
            @Param("communityType") CommunityType communityType,
            Pageable pageable);

    @Modifying
    @Query(
            "UPDATE PostMeta pm SET pm.viewCount = pm.viewCount + :viewCount WHERE pm.post.id = :postId")
    void updateViewCount(@Param("postId") Long postId, @Param("viewCount") int viewCount);
}
