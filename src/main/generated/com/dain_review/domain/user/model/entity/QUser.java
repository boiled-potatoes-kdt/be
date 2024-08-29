package com.dain_review.domain.user.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 156963922L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final ListPath<com.dain_review.domain.application.model.entity.Application, com.dain_review.domain.application.model.entity.QApplication> applicationList = this.<com.dain_review.domain.application.model.entity.Application, com.dain_review.domain.application.model.entity.QApplication>createList("applicationList", com.dain_review.domain.application.model.entity.Application.class, com.dain_review.domain.application.model.entity.QApplication.class, PathInits.DIRECT2);

    public final ListPath<com.dain_review.domain.campaign.model.entity.Campaign, com.dain_review.domain.campaign.model.entity.QCampaign> campaignList = this.<com.dain_review.domain.campaign.model.entity.Campaign, com.dain_review.domain.campaign.model.entity.QCampaign>createList("campaignList", com.dain_review.domain.campaign.model.entity.Campaign.class, com.dain_review.domain.campaign.model.entity.QCampaign.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final QEnterpriser enterpriser;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QInfluencer influencer;

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath joinPath = createString("joinPath");

    public final BooleanPath marketing = createBoolean("marketing");

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final BooleanPath penalty = createBoolean("penalty");

    public final StringPath phone = createString("phone");

    public final NumberPath<Long> point = createNumber("point", Long.class);

    public final StringPath postalCode = createString("postalCode");

    public final ListPath<com.dain_review.domain.post.model.entity.Post, com.dain_review.domain.post.model.entity.QPost> posts = this.<com.dain_review.domain.post.model.entity.Post, com.dain_review.domain.post.model.entity.QPost>createList("posts", com.dain_review.domain.post.model.entity.Post.class, com.dain_review.domain.post.model.entity.QPost.class, PathInits.DIRECT2);

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<com.dain_review.domain.user.model.entity.enums.Role> role = createEnum("role", com.dain_review.domain.user.model.entity.enums.Role.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.enterpriser = inits.isInitialized("enterpriser") ? new QEnterpriser(forProperty("enterpriser"), inits.get("enterpriser")) : null;
        this.influencer = inits.isInitialized("influencer") ? new QInfluencer(forProperty("influencer"), inits.get("influencer")) : null;
    }

}

