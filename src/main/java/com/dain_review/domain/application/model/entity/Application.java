package com.dain_review.domain.application.model.entity;


import com.dain_review.domain.application.model.type.State;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.user.model.entity.User;
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
public class Application extends BaseEntity { // 체험단 신청

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 하나의 유저의 여러 개의 체험단 신청

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign; // 하나의 캠페인에 여러 개의 체험단 신청

    private String message;

    @Enumerated(EnumType.STRING)
    private State state;
}
