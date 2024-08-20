package com.dain_review.domain.campaign.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignPreviewResponse {
    private Long id;
    private String name;
    private String reward;
    private String region1;
    private String region2;
    private Integer point;
    private String type;
    private String platform;
    private String label;
    private String capacity;
    private String applicant;
    private String campaignImage;
    private LocalDateTime applicationStartDate;
    private LocalDateTime applicationEndDate;
    private LocalDateTime experienceStartDate;
    private LocalDateTime experienceEndDate;
    private boolean isLike;
}
