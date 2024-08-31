package com.dain_review.domain.post.repository;


import com.dain_review.domain.post.exception.PostException;
import com.dain_review.domain.post.exception.errortype.PostErrorCode;
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
    default Post getPostById(Long id) {
        return findById(id).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }

    // 커뮤니티 전체 게시글 조회 (최신순 정렬)
    @Query(
            "SELECT p FROM Post p LEFT JOIN FETCH p.postMeta WHERE p.categoryType = :categoryType ORDER BY p.createdAt DESC")
    Page<Post> findByCategoryType(
            @Param("categoryType") CategoryType categoryType, Pageable pageable);

    // 게시글을 카테고리로 분류하여 목록 조회 (최신순 정렬)
    @Query(
            "SELECT p FROM Post p JOIN FETCH p.postMeta WHERE p.categoryType = :categoryType AND p.communityType = :communityType ORDER BY p.createdAt DESC")
    Page<Post> findByCategoryTypeAndCommunityType(
            @Param("categoryType") CategoryType categoryType,
            @Param("communityType") CommunityType communityType,
            Pageable pageable);

    // 검색 기능: 제목, 작성자 이름, 내용에 대해 검색 (카테고리 필터 포함, 최신순 정렬)
    @Query(
            "SELECT p FROM Post p LEFT JOIN FETCH p.user u LEFT JOIN FETCH p.postMeta WHERE p.categoryType = :categoryType AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword% OR u.name LIKE %:keyword%) ORDER BY p.createdAt DESC")
    Page<Post> searchByKeyword(
            @Param("categoryType") CategoryType categoryType,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Modifying
    @Query(
            "UPDATE PostMeta pm SET pm.viewCount = pm.viewCount + :viewCount WHERE pm.post.id = :postId")
    void updateViewCount(@Param("postId") Long postId, @Param("viewCount") int viewCount);

    // 해당 사용자가 작성한 게시글들 삭제
    @Modifying
    @Query("DELETE FROM Post p WHERE p.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
