package com.dain_review.domain.auth.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class GoogleUserInfo {
    private final String id;
    private final String email;
    private final String name;
    private final String picture;
    public GoogleUserInfo(Map<String, Object> attributes) {
        this.id = (String) attributes.get("sub");
        this.email = (String) attributes.get("email");
        this.name = (String) attributes.get("name");
        this.picture = (String) attributes.get("picture");
    }

}
