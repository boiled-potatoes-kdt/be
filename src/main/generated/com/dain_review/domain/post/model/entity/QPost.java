package com.dain_review.domain.post.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 2089206460L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    public final EnumPath<com.dain_review.domain.post.model.entity.enums.CategoryType> categoryType = createEnum("categoryType", com.dain_review.domain.post.model.entity.enums.CategoryType.class);

    public final ListPath<com.dain_review.domain.comment.model.entity.Comment, com.dain_review.domain.comment.model.entity.QComment> commentList = this.<com.dain_review.domain.comment.model.entity.Comment, com.dain_review.domain.comment.model.entity.QComment>createList("commentList", com.dain_review.domain.comment.model.entity.Comment.class, com.dain_review.domain.comment.model.entity.QComment.class, PathInits.DIRECT2);

    public final EnumPath<com.dain_review.domain.post.model.entity.enums.CommunityType> communityType = createEnum("communityType", com.dain_review.domain.post.model.entity.enums.CommunityType.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath imageUrl = createString("imageUrl");

    public final QPostMeta postMeta;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.dain_review.domain.user.model.entity.QUser user;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.postMeta = inits.isInitialized("postMeta") ? new QPostMeta(forProperty("postMeta"), inits.get("postMeta")) : null;
        this.user = inits.isInitialized("user") ? new com.dain_review.domain.user.model.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

