package com.dain_review.domain.user.model.entity;


import com.dain_review.domain.user.model.type.SnsType;
import com.dain_review.global.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    private String url;
}
