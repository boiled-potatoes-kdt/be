package com.dain_review.domain.user.model.request;


import com.dain_review.domain.user.model.entity.enums.Gender;
import java.time.LocalDate;

public record InfluencerExtraRegisterRequest(
        String profileImage,
        LocalDate birthday,
        Gender gender,
        String address,
        String addressDetail,
        String postalCode) {}
