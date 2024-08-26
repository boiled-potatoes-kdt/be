package com.dain_review.domain.user.model.response;


import com.dain_review.domain.user.model.entity.enums.SnsType;

public record SnsResponse(SnsType snsType, String url) {}
