package com.dain_review.domain.user.model.request;


import com.dain_review.global.validation.type.AllOrNone;
import jakarta.validation.constraints.NotBlank;

@AllOrNone(fields = {"address", "addressDetail", "postalCode"})
@AllOrNone(fields = {"oldPassword", "newPassword"})
public record EnterpriserChangeRequest(
        String oldPassword,
        String newPassword,
        @NotBlank String name,
        @NotBlank String nickname,
        @NotBlank String phone,
        String address,
        String addressDetail,
        String postalCode) {}
