package com.dain_review.domain.select.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SelectInfluencerRequest(@NotNull Long userId, @NotBlank Long campaignId) {}
