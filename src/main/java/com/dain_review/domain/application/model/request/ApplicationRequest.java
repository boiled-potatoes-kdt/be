package com.dain_review.domain.application.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApplicationRequest(@NotNull Long campaignId, @NotBlank String message) {}
