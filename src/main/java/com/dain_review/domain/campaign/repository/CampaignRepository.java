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

//<<<<<<< HEAD
//public interface CampaignRepository extends JpaRepository<Campaign, Long> {
//
//    // Todo 추천순, 인기순, 마김임박순, 최신순 정렬 조회
//    // 필터링 요소: 카테고리, 유형, 플랫폼
//
//    // 프리미엄 체험단 조회
//    // 정렬 1.포인트 많은 순 2.마감 임박 순 3.모집인원 많은 순 4. 승인일시 순(최근 승인된 순)
//    @Query("select C from Campaign C where (:label is null or C.label=:label)" +
//            "and (:category is null or C.category=:category)" +
//            "and (:type is null or C.type=:type)" +
//            "and (:platform is null or C.platform=:platform)" +
//            "order by C.point desc, C.applicationEndDate, C.capacity desc, C.approvalDate desc")
//    Page<Campaign> findByLabel(
//            @Param("label") Label label,
//            @Param("category")Category category,
//            @Param("type")Type type,
//            @Param("platform")Platform platform,
//            Pageable pageable
//    );
//
//    // 인기 체험단 조회
//    // 정렬 1.지원자 많은 순 2.모집인원 많은 순 3.승인일시 순
//    @Query("select C from Campaign C where (:category is null or C.category=:category)" +
//            "and (:type is null or C.type=:type)" +
//            "and (:platform is null or C.platform=:platform)" +
//            "order by C.applicant desc, C.capacity desc, C.approvalDate desc")
//    Page<Campaign> orderByApplicant(
//            @Param("category")Category category,
//            @Param("type")Type type,
//            @Param("platform")Platform platform,
//            Pageable pageable
//    );
//
//    // 신규 체험단 조회
//    // 정렬 1.모집 시작일 늦은 순 2.승인일시 순
//    @Query("select C from Campaign C where (:category is null or C.category=:category)" +
//            "and (:type is null or C.type=:type)" +
//            "and (:platform is null or C.platform=:platform)" +
//            "order by C.applicationStartDate desc, C.approvalDate desc")
//    Page<Campaign> orderByApprovalDate(
//            @Param("category")Category category,
//            @Param("type")Type type,
//            @Param("platform")Platform platform,
//            Pageable pageable
//    );
//
//    // 마감임박 체험단 조회
//    // 정렬 1.모집 마감일 적게 남은 순 2.신청자 많은 순 3.승인일시 순
//    @Query("select C from Campaign C where (:category is null or C.category=:category)" +
//            "and (:type is null or C.type=:type)" +
//            "and (:platform is null or C.platform=:platform)" +
//            "order by C.applicationEndDate, C.applicant desc, C.approvalDate desc")
//    Page<Campaign> orderByImminentDueDate(
//            @Param("category")Category category,
//            @Param("type")Type type,
//            @Param("platform")Platform platform,
//            Pageable pageable
//    );
//
//=======
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
