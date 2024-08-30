package com.dain_review.domain.campaign.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLabelOrdering is a Querydsl query type for LabelOrdering
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLabelOrdering extends EntityPathBase<LabelOrdering> {

    private static final long serialVersionUID = -1707119140L;

    public static final QLabelOrdering labelOrdering = new QLabelOrdering("labelOrdering");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath label = createString("label");

    public final NumberPath<Integer> ordering = createNumber("ordering", Integer.class);

    public QLabelOrdering(String variable) {
        super(LabelOrdering.class, forVariable(variable));
    }

    public QLabelOrdering(Path<? extends LabelOrdering> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLabelOrdering(PathMetadata metadata) {
        super(LabelOrdering.class, metadata);
    }

}

