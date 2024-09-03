package com.dain_review.domain.Image.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QImageFile is a Querydsl query type for ImageFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImageFile extends EntityPathBase<ImageFile> {

    private static final long serialVersionUID = 136071491L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QImageFile imageFile = new QImageFile("imageFile");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath fileName = createString("fileName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.dain_review.domain.post.model.entity.QPost post;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QImageFile(String variable) {
        this(ImageFile.class, forVariable(variable), INITS);
    }

    public QImageFile(Path<? extends ImageFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QImageFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QImageFile(PathMetadata metadata, PathInits inits) {
        this(ImageFile.class, metadata, inits);
    }

    public QImageFile(Class<? extends ImageFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new com.dain_review.domain.post.model.entity.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

