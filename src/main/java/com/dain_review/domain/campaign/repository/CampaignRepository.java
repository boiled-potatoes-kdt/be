package com.dain_review.domain.campaign.repository;


import com.dain_review.domain.campaign.exception.CampaignException;
import com.dain_review.domain.campaign.exception.errortype.CampaignErrorCode;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.entity.enums.Platform;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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
                    + "WHERE c.campaignState = :campaignState "
                    + "AND c.platform = :platform "
                    + "AND c.businessName LIKE %:keyword% "
                    + "AND c.user.id = :userId "
                    + "AND c.isDeleted = false "
                    + "ORDER BY c.id DESC")
    Page<Campaign> findByStateAndPlatformAndNameContainingAndUserId(
            @Param("campaignState") CampaignState campaignState,
            @Param("platform") Platform platform,
            @Param("keyword") String keyword,
            @Param("userId") Long userId,
            Pageable pageable);

    // @EntityGraph 적용하여 Lazy 로딩된 컬렉션을 즉시 로딩
    @EntityGraph(attributePaths = {"keywords", "availableDays"})
    Optional<Campaign> findWithDetailsById(Long id);
}
