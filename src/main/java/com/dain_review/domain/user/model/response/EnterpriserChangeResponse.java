package com.dain_review.domain.user.model.response;

public record EnterpriserChangeResponse(
        String email,
        String name,
        String phone,
        String address,
        String addressDetail,
        String postalCode) {}
