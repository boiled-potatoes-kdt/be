package com.dain_review.domain.user.model.request;


import com.dain_review.global.validation.type.AllOrNone;

@AllOrNone(fields = {"address", "addressDetail", "postalCode"})
public record EnterpriserExtraRegisterRequest(
        String address, String addressDetail, String postalCode) {}
