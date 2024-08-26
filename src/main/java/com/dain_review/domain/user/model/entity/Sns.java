package com.dain_review.domain.user.model.entity;


import com.dain_review.domain.user.model.entity.enums.SnsType;
import com.dain_review.domain.user.model.response.SnsResponse;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Sns extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    @ManyToOne
    @JoinColumn(name = "influencer_id")
    private Influencer influencer;

    private String url;

    public SnsResponse toSnsResponse() {
        return new SnsResponse(this.snsType, this.url);
    }
}
