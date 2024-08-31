package com.dain_review.domain.campaign.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCampaign is a Querydsl query type for Campaign
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCampaign extends EntityPathBase<Campaign> {

    private static final long serialVersionUID = -1559584868L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCampaign campaign = new QCampaign("campaign");

    public final com.dain_review.global.model.entity.QBaseEntity _super = new com.dain_review.global.model.entity.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final DateTimePath<java.time.LocalDateTime> announcementDate = createDateTime("announcementDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> applicationEndDate = createDateTime("applicationEndDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> applicationStartDate = createDateTime("applicationStartDate", java.time.LocalDateTime.class);

    public final SetPath<String, StringPath> availableDays = this.<String, StringPath>createSet("availableDays", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath businessName = createString("businessName");

    public final EnumPath<com.dain_review.domain.campaign.model.entity.enums.CampaignState> campaignState = createEnum("campaignState", com.dain_review.domain.campaign.model.entity.enums.CampaignState.class);

    public final NumberPath<Integer> capacity = createNumber("capacity", Integer.class);

    public final EnumPath<com.dain_review.domain.campaign.model.entity.enums.Category> category = createEnum("category", com.dain_review.domain.campaign.model.entity.enums.Category.class);

    public final StringPath city = createString("city");

    public final StringPath contactNumber = createString("contactNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> currentApplicants = createNumber("currentApplicants", Integer.class);

    public final StringPath district = createString("district");

    public final DateTimePath<java.time.LocalDateTime> experienceEndDate = createDateTime("experienceEndDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> experienceStartDate = createDateTime("experienceStartDate", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final SetPath<String, StringPath> keywords = this.<String, StringPath>createSet("keywords", String.class, StringPath.class, PathInits.DIRECT2);

    public final EnumPath<com.dain_review.domain.campaign.model.entity.enums.Label> label = createEnum("label", com.dain_review.domain.campaign.model.entity.enums.Label.class);

    public final QLabelOrdering labelOrdering;

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final EnumPath<com.dain_review.domain.campaign.model.entity.enums.Platform> platform = createEnum("platform", com.dain_review.domain.campaign.model.entity.enums.Platform.class);

    public final BooleanPath pointPayment = createBoolean("pointPayment");

    public final NumberPath<Integer> pointPerPerson = createNumber("pointPerPerson", Integer.class);

    public final NumberPath<Integer> postalCode = createNumber("postalCode", Integer.class);

    public final StringPath requirement = createString("requirement");

    public final DateTimePath<java.time.LocalDateTime> reviewDate = createDateTime("reviewDate", java.time.LocalDateTime.class);

    public final StringPath serviceProvided = createString("serviceProvided");

    public final NumberPath<Integer> totalPoints = createNumber("totalPoints", Integer.class);

    public final EnumPath<com.dain_review.domain.campaign.model.entity.enums.Type> type = createEnum("type", com.dain_review.domain.campaign.model.entity.enums.Type.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.dain_review.domain.user.model.entity.QUser user;

    public QCampaign(String variable) {
        this(Campaign.class, forVariable(variable), INITS);
    }

    public QCampaign(Path<? extends Campaign> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCampaign(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCampaign(PathMetadata metadata, PathInits inits) {
        this(Campaign.class, metadata, inits);
    }

    public QCampaign(Class<? extends Campaign> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.labelOrdering = inits.isInitialized("labelOrdering") ? new QLabelOrdering(forProperty("labelOrdering")) : null;
        this.user = inits.isInitialized("user") ? new com.dain_review.domain.user.model.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

