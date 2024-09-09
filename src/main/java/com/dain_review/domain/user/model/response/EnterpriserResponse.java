package com.dain_review.domain.user.model.response;


import com.dain_review.domain.user.model.entity.User;

public record EnterpriserResponse(String profileImage, String nickname) {

    // 완성
    public static EnterpriserResponse from(User user) {
        return new EnterpriserResponse(user.getProfileImage(), user.getNickname());
    }
}
