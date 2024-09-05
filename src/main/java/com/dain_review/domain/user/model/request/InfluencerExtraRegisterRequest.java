package com.dain_review.domain.user.model.request;


import com.dain_review.domain.user.model.entity.enums.Gender;
import com.dain_review.global.validation.type.AllOrNone;
import java.time.LocalDate;

@AllOrNone(fields = {"address", "addressDetail", "postalCode"})
public record InfluencerExtraRegisterRequest(
        String profileImage,
        LocalDate birthday,
        Gender gender,
        String address,
        String addressDetail,
        String postalCode) {}
