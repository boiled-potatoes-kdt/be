package com.dain_review.domain.user.model.response;


import com.dain_review.domain.user.model.entity.enums.Gender;
import java.time.LocalDate;
import java.util.List;

public record InfluencerChangeResponse(
        String name,
        String profileImage,
        List<SnsResponse> snsResponseList,
        Integer likeCnt,
        String email,
        String phone,
        String nickname,
        String address,
        String addressDetail,
        String postalCode,
        LocalDate birthday,
        Gender gender) {}
