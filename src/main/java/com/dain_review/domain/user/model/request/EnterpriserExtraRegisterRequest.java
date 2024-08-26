package com.dain_review.domain.user.model.request;


import com.dain_review.global.validation.type.AddressFieldsValidation;

@AddressFieldsValidation
public record EnterpriserExtraRegisterRequest(
        String profileImage, String address, String addressDetail, String postalCode) {}
