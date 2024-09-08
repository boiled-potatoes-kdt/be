package com.dain_review.domain.post.repository;

import com.dain_review.domain.post.model.entity.QPost;
import com.dain_review.domain.post.model.entity.enums.CategoryType;
import com.dain_review.domain.post.model.entity.enums.CommunityType;
import com.dain_review.domain.post.model.entity.enums.FollowType;
import com.dain_review.domain.user.model.entity.enums.Role;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Long findPreviousPost(Long postId, Role role, CategoryType categoryType, CommunityType communityType, FollowType followType, String keyword) {
        QPost post = QPost.post;

        BooleanBuilder builder = new BooleanBuilder();

        if(categoryType != null) {
            builder.and(post.categoryType.eq(categoryType));

            if(categoryType == CategoryType.COMMUNITY) {
                builder.and(post.user.role.eq(role));
            }
        }

        if(communityType != null) {
            builder.and(post.communityType.eq(communityType));
        }

        if(followType != null) {
            builder.and(post.followType.eq(followType));
        }

        if(keyword != null) {
            builder.and(post.title.containsIgnoreCase(keyword));
        }

        builder.and(post.deleted.isFalse());

        var query = queryFactory
                .selectFrom(post)
                .where(
                        post.id.gt(postId),
                        builder
                )
                .orderBy(post.id.asc())
                .fetchFirst();

        return (query == null) ? null : query.getId();
    }

    @Override
    public Long findNextPost(Long postId, Role role, CategoryType categoryType, CommunityType communityType, FollowType followType, String keyword) {
        QPost post = QPost.post;

        BooleanBuilder builder = new BooleanBuilder();

        if(categoryType != null) {
            builder.and(post.categoryType.eq(categoryType));
        }

        if(communityType != null) {
            builder.and(post.communityType.eq(communityType));

            if(categoryType == CategoryType.COMMUNITY) {
                builder.and(post.user.role.eq(role));
            }
        }

        if(followType != null) {
            builder.and(post.followType.eq(followType));
        }

        if(keyword != null) {
            builder.and(post.title.containsIgnoreCase(keyword));
        }

        builder.and(post.deleted.isFalse());

        var query = queryFactory
                .selectFrom(post)
                .where(
                        post.id.lt(postId),
                        builder
                )
                .orderBy(post.id.desc())
                .fetchFirst();

        return (query == null) ? null : query.getId();
    }
}
