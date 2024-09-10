package com.dain_review.domain.user.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEnterpriser is a Querydsl query type for Enterpriser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEnterpriser extends EntityPathBase<Enterpriser> {

    private static final long serialVersionUID = 1373420234L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEnterpriser enterpriser = new QEnterpriser("enterpriser");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    public final StringPath company = createString("company");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public QEnterpriser(String variable) {
        this(Enterpriser.class, forVariable(variable), INITS);
    }

    public QEnterpriser(Path<? extends Enterpriser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEnterpriser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEnterpriser(PathMetadata metadata, PathInits inits) {
        this(Enterpriser.class, metadata, inits);
    }

    public QEnterpriser(Class<? extends Enterpriser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

