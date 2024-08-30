package com.dain_review.domain.post.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttachedFile is a Querydsl query type for AttachedFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttachedFile extends EntityPathBase<AttachedFile> {

    private static final long serialVersionUID = 415417180L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttachedFile attachedFile = new QAttachedFile("attachedFile");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath fileName = createString("fileName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QPost post;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAttachedFile(String variable) {
        this(AttachedFile.class, forVariable(variable), INITS);
    }

    public QAttachedFile(Path<? extends AttachedFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttachedFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttachedFile(PathMetadata metadata, PathInits inits) {
        this(AttachedFile.class, metadata, inits);
    }

    public QAttachedFile(Class<? extends AttachedFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

