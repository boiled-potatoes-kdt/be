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

import java.util.List;

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

//     프리미엄 체험단 조회
//     todo 정렬 1.포인트 많은 순 2.마감 임박 순 3.모집인원 많은 순 4. 승인일시 순(최근 승인된 순)
    @Query("select C from Campaign C where C.label= 'PREMIUM'" +
            "and C.campaignState = 'RECRUITING'" +
            "order by C.pointPerPerson desc, C.applicationEndDate, C.capacity desc " +
            "limit 8")
    List<Campaign> findPremiumCampaigns();

    // 인기 체험단 조회
    // todo 정렬 1.지원자 많은 순 2.모집인원 많은 순 3.승인일시 순
    @Query("select C from Campaign C where C.campaignState = 'RECRUITING'" +
            "order by C.currentApplicants desc, C.capacity desc " +
            "limit 8")
    List<Campaign> findPopularCampaigns();


    // 신규 체험단 조회
    // todo 정렬 1.모집 시작일 늦은 순 2.승인일시 순
    @Query("select C from Campaign C where C.campaignState = 'RECRUITING'" +
            "order by C.applicationStartDate desc " +
            "limit 4")
    List<Campaign> findNewestCampaigns();


    // 마감임박 체험단 조회
    // todo 정렬 1.모집 마감일 적게 남은 순 2.신청자 많은 순 3.승인일시 순
    @Query("select C from Campaign C where C.campaignState = 'RECRUITING'" +
            "order by C.applicationEndDate asc, C.currentApplicants desc " +
            "limit 4")
    List<Campaign> findImminentDueDateCampaigns();

}
