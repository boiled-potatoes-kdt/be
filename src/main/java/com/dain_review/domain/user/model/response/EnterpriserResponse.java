package com.dain_review.domain.user.model.response;


import com.dain_review.domain.user.model.entity.User;

public record EnterpriserResponse(String profileImage, String nickname) {

    // 완성
    public static EnterpriserResponse from(User user, String imageUrl) {
        return new EnterpriserResponse(imageUrl, user.getNickname());
    }
}
