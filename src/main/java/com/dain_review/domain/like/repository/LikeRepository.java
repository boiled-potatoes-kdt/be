package com.dain_review.domain.like.repository;


import com.dain_review.domain.like.model.entity.Like;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndCampaignId(Long userId, Long campaignId);

    @EntityGraph(attributePaths = {"campaign.availableDays", "campaign.keywords"})
    Page<Like> findByUserIdAndIsLiked(Long userId, boolean isLiked, Pageable pageable);
}
