package com.dain_review.domain.user.model.request;


import com.dain_review.global.validation.type.AddressFieldsValidation;

@AddressFieldsValidation
public record EnterpriserChangeRequest(
        String oldPassword,
        String newPassword,
        String name,
        String nickname,
        String phone,
        String address,
        String addressDetail,
        String postalCode) {}
