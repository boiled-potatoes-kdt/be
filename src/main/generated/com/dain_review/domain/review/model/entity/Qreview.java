package com.dain_review.domain.review.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * Qreview is a Querydsl query type for review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class Qreview extends EntityPathBase<review> {

    private static final long serialVersionUID = 813221900L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final Qreview review = new Qreview("review");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    public final com.dain_review.domain.application.model.entity.QApplication application;

    public final com.dain_review.domain.campaign.model.entity.QCampaign campaign;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath url = createString("url");

    public final com.dain_review.domain.user.model.entity.QUser user;

    public Qreview(String variable) {
        this(review.class, forVariable(variable), INITS);
    }

    public Qreview(Path<? extends review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public Qreview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public Qreview(PathMetadata metadata, PathInits inits) {
        this(review.class, metadata, inits);
    }

    public Qreview(Class<? extends review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.application = inits.isInitialized("application") ? new com.dain_review.domain.application.model.entity.QApplication(forProperty("application"), inits.get("application")) : null;
        this.campaign = inits.isInitialized("campaign") ? new com.dain_review.domain.campaign.model.entity.QCampaign(forProperty("campaign"), inits.get("campaign")) : null;
        this.user = inits.isInitialized("user") ? new com.dain_review.domain.user.model.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

