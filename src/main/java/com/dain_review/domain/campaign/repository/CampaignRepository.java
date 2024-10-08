package com.dain_review.domain.campaign.repository;


import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampaignRepository
        extends JpaRepository<Campaign, Long>, CampaignRepositoryCustom {

    default Campaign getCampaignById(Long id) {
        return findById(id)
                .orElseThrow(() -> new CampaignException(CampaignErrorCode.CAMPAIGN_NOT_FOUND));
    }

    @Query(
            "SELECT c FROM Campaign c "
                    + "WHERE (:campaignState IS NULL OR c.campaignState = :campaignState) "
                    + "AND (:platform IS NULL OR c.platform = :platform) "
                    + "AND (:keyword IS NULL OR c.businessName LIKE %:keyword%) "
                    + "AND c.user.id = :userId "
                    + "AND c.isDeleted = false ")
    Page<Campaign> findByStateAndPlatformAndNameContainingAndUserId(
            @Param("campaignState") CampaignState campaignState,
            @Param("platform") Platform platform,
            @Param("keyword") String keyword,
            @Param("userId") Long userId,
            Pageable pageable);
}
