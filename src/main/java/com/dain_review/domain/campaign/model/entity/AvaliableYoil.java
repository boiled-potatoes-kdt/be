package com.dain_review.domain.campaign.model.entity;


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
public class AvaliableYoil extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    private String yoil;
}
