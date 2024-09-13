package com.dain_review.domain.user.model.request;


import com.dain_review.domain.user.model.entity.enums.SnsType;

public record SingUpSns(SnsType snsType, String url) {}
