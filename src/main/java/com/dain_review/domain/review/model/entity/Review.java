package com.dain_review.domain.review.model.entity;


import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
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
public class Review extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    private String url;
}
