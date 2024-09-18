package com.dain_review.domain.user.model.request;

public record PasswordChangeRequest(String email, String name, String impId, String password) {}
