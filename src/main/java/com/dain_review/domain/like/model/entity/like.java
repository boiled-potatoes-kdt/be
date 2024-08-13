package com.dain_review.domain.like.model.entity;


import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
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
public class like extends BaseEntity {

    private Long userId;
    @ManyToOne private Campaign campaign;
}
