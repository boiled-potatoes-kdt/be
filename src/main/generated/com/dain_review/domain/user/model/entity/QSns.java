package com.dain_review.domain.user.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSns is a Querydsl query type for Sns
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSns extends EntityPathBase<Sns> {

    private static final long serialVersionUID = -687675375L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSns sns = new QSns("sns");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QInfluencer influencer;

    public final EnumPath<com.dain_review.domain.user.model.entity.enums.SnsType> snsType = createEnum("snsType", com.dain_review.domain.user.model.entity.enums.SnsType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath url = createString("url");

    public QSns(String variable) {
        this(Sns.class, forVariable(variable), INITS);
    }

    public QSns(Path<? extends Sns> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSns(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSns(PathMetadata metadata, PathInits inits) {
        this(Sns.class, metadata, inits);
    }

    public QSns(Class<? extends Sns> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.influencer = inits.isInitialized("influencer") ? new QInfluencer(forProperty("influencer"), inits.get("influencer")) : null;
    }

}

