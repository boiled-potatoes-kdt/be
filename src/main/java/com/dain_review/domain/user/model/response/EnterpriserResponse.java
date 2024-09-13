package com.dain_review.domain.user.model.response;


import com.dain_review.domain.user.model.entity.User;

public record EnterpriserResponse(String profileImageUrl, String nickname) {

    // 완성
    public static EnterpriserResponse from(User user) {
        return new EnterpriserResponse(user.getProfileImageUrl(), user.getNickname());
    }
}
