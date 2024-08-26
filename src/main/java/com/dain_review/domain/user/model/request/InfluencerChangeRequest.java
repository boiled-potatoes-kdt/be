package com.dain_review.domain.user.model.request;


import com.dain_review.domain.user.model.entity.enums.Gender;
import java.time.LocalDate;
import java.util.List;

public record InfluencerChangeRequest(
        String oldPassword,
        String newPassword,
        String name,
        String nickname,
        String phone,
        List<SnsRequest> snsRequestList,
        String address,
        String addressDetail,
        String postalCode,
        LocalDate birthday,
        Gender gender) {}
