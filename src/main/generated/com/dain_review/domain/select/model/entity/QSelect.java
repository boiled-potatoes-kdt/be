package com.dain_review.domain.select.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSelect is a Querydsl query type for Select
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSelect extends EntityPathBase<Select> {

    private static final long serialVersionUID = -910494348L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSelect select = new QSelect("select1");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    public final com.dain_review.domain.campaign.model.entity.QCampaign campaign;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.dain_review.domain.user.model.entity.QUser user;

    public QSelect(String variable) {
        this(Select.class, forVariable(variable), INITS);
    }

    public QSelect(Path<? extends Select> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSelect(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSelect(PathMetadata metadata, PathInits inits) {
        this(Select.class, metadata, inits);
    }

    public QSelect(Class<? extends Select> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.campaign = inits.isInitialized("campaign") ? new com.dain_review.domain.campaign.model.entity.QCampaign(forProperty("campaign"), inits.get("campaign")) : null;
        this.user = inits.isInitialized("user") ? new com.dain_review.domain.user.model.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

