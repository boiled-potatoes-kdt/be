package com.dain_review.domain.review.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewAttachedFile is a Querydsl query type for ReviewAttachedFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewAttachedFile extends EntityPathBase<ReviewAttachedFile> {

    private static final long serialVersionUID = -604071028L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewAttachedFile reviewAttachedFile = new QReviewAttachedFile("reviewAttachedFile");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath fileName = createString("fileName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QReview review;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReviewAttachedFile(String variable) {
        this(ReviewAttachedFile.class, forVariable(variable), INITS);
    }

    public QReviewAttachedFile(Path<? extends ReviewAttachedFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewAttachedFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewAttachedFile(PathMetadata metadata, PathInits inits) {
        this(ReviewAttachedFile.class, metadata, inits);
    }

    public QReviewAttachedFile(Class<? extends ReviewAttachedFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
    }

}

