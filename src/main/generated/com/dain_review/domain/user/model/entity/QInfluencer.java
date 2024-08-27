package com.dain_review.domain.user.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInfluencer is a Querydsl query type for Influencer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInfluencer extends EntityPathBase<Influencer> {

    private static final long serialVersionUID = -426170524L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInfluencer influencer = new QInfluencer("influencer");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    public final DatePath<java.time.LocalDate> birthday = createDate("birthday", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.dain_review.domain.user.model.entity.enums.Gender> gender = createEnum("gender", com.dain_review.domain.user.model.entity.enums.Gender.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<Sns, QSns> snsList = this.<Sns, QSns>createList("snsList", Sns.class, QSns.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public QInfluencer(String variable) {
        this(Influencer.class, forVariable(variable), INITS);
    }

    public QInfluencer(Path<? extends Influencer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInfluencer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInfluencer(PathMetadata metadata, PathInits inits) {
        this(Influencer.class, metadata, inits);
    }

    public QInfluencer(Class<? extends Influencer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

