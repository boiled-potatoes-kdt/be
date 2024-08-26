package com.dain_review.domain.user.model.request;


import com.dain_review.domain.user.model.entity.Influencer;
import com.dain_review.domain.user.model.entity.Sns;
import com.dain_review.domain.user.model.entity.enums.SnsType;

public record SnsRequest(SnsType snsType, String url) {

    public Sns toSns(Influencer influencer) {
        return Sns.builder().snsType(this.snsType).url(this.url).influencer(influencer).build();
    }
}
