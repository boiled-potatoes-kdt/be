package com.dain_review.domain.user.model.request;


import jakarta.validation.constraints.NotBlank;

public record ProfileChangeRequest(@NotBlank String profileImage) {}
