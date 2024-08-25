package com.dain_review.domain.user.model.request;

public record EnterpriserChangeRequest(
        String oldPassword,
        String newPassword,
        String name,
        String nickname,
        String phone,
        String address,
        String addressDetail,
        String postalCode) {}
