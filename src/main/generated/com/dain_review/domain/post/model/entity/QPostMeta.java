package com.dain_review.domain.post.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostMeta is a Querydsl query type for PostMeta
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostMeta extends EntityPathBase<PostMeta> {

    private static final long serialVersionUID = -2116841759L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostMeta postMeta = new QPostMeta("postMeta");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    public final NumberPath<Long> commentCount = createNumber("commentCount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QPost post;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QPostMeta(String variable) {
        this(PostMeta.class, forVariable(variable), INITS);
    }

    public QPostMeta(Path<? extends PostMeta> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostMeta(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostMeta(PathMetadata metadata, PathInits inits) {
        this(PostMeta.class, metadata, inits);
    }

    public QPostMeta(Class<? extends PostMeta> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

