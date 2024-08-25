package com.dain_review.domain.application.repository;


import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query(
            "SELECT a FROM Application a "
                    + "WHERE (:campaignState IS NULL OR a.campaign.campaignState = :campaignState) "
                    + "AND (:platform IS NULL OR a.campaign.platform = :platform) "
                    + "AND (:keyword IS NULL OR a.campaign.name LIKE %:keyword%) "
                    + "AND a.campaign.isDeleted = false "
                    + "AND a.user.id = :userId "
                    + "AND a.isDeleted = false")
    Page<Application> findByStateAndPlatformAndNameContainingAndUserId(
            @Param("campaignState") CampaignState campaignState,
            @Param("platform") Platform platform,
            @Param("keyword") String keyword,
            @Param("userId") Long userId,
            Pageable pageable);

    Optional<Application> findByIdAndUserId(Long applicationId, Long userId);

    @Modifying
    @Query("UPDATE Application a SET a.isDeleted = true WHERE a.id = :applicationId")
    void softDeleteById(@Param("applicationId") Long applicationId);
}
