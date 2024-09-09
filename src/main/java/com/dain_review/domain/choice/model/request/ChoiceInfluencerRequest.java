package com.dain_review.domain.choice.model.request;


import jakarta.validation.constraints.NotNull;

public record ChoiceInfluencerRequest(@NotNull Long userId, @NotNull Long campaignId) {}
