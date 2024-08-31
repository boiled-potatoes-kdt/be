package com.dain_review.domain.campaign.repository;


import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.QCampaign;
import com.dain_review.domain.campaign.model.entity.enums.SortBy;
import com.dain_review.domain.campaign.model.request.CampaignSearchRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CampaignRepositoryImpl implements CampaignRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Campaign> searchCampaigns(CampaignSearchRequest searchRequest, Pageable pageable) {
        QCampaign campaign = QCampaign.campaign;

        BooleanBuilder builder = new BooleanBuilder();

        // 검색 조건 필터링 로직
        if (searchRequest.category() != null) {
            builder.and(campaign.category.eq(searchRequest.category()));
        }

        if (searchRequest.platform() != null) {
            builder.and(campaign.platform.eq(searchRequest.platform()));
        }

        if (searchRequest.type() != null) {
            builder.and(campaign.type.eq(searchRequest.type()));
        }

        if (searchRequest.campaignState() != null) {
            builder.and(campaign.campaignState.eq(searchRequest.campaignState()));
        }

        if (searchRequest.keyword() != null) {
            builder.and(campaign.businessName.containsIgnoreCase(searchRequest.keyword()));
        }

        if (searchRequest.cities() != null && !searchRequest.cities().isEmpty()) {
            builder.and(campaign.city.in(searchRequest.cities()));
        }

        if (searchRequest.districts() != null && !searchRequest.districts().isEmpty()) {
            builder.and(campaign.district.in(searchRequest.districts()));
        }

        builder.and(campaign.isDeleted.isFalse());

        // 쿼리 생성
        var query =
                queryFactory
                        .selectFrom(campaign)
                        .where(builder)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize());

        // 정렬 조건 설정 - created_at 대신 id로 정렬
        if (searchRequest.sortBy() == SortBy.POPULAR) { // 인기순
            query.orderBy(
                    campaign.currentApplicants.desc(),
                    campaign.labelOrdering.ordering.asc(),
                    campaign.id.desc());
        } else if (searchRequest.sortBy() == SortBy.CLOSING_SOON) { // 마감임박순
            query.orderBy(
                    campaign.applicationEndDate.asc(),
                    campaign.experienceStartDate.asc(),
                    campaign.labelOrdering.ordering.asc(),
                    campaign.id.desc());
        } else if (searchRequest.sortBy() == SortBy.NEWEST) { // 최신순
            query.orderBy(campaign.id.desc(), campaign.labelOrdering.ordering.asc());
        } else { // 기본 정렬 값은 추천순
            query.orderBy(
                    campaign.totalPoints.desc(),
                    campaign.currentApplicants.asc(),
                    campaign.labelOrdering.ordering.asc(),
                    campaign.id.desc());
        }

        List<Campaign> results = query.fetch();
        long total = queryFactory.selectFrom(campaign).where(builder).fetchCount();
        return new PageImpl<>(results, pageable, total);
    }
}
