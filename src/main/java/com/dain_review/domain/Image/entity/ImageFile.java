package com.dain_review.domain.Image.entity;


import com.dain_review.domain.Image.entity.enums.ContentType;
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
public class ImageFile extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    private String fileName;

    private String imageUrl;
}
