package com.dain_review.domain.campaign.model.response;


import com.dain_review.domain.campaign.model.entity.Campaign;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CampaignResponse {
    private Long id;
    private String businessName;
    private String imageUrl;
    private String contactNumber;
    private String address;
    private Integer postalCode;
    private Double latitude;
    private Double longitude;
    private Set<String> availableDays;
    private String type;
    private String category;
    private String platform;
    private String label;
    private Integer capacity;
    private Integer currentApplicants;
    private String serviceProvided;
    private String requirement;
    private Set<String> keywords;
    private Boolean pointPayment;
    private Integer pointPerPerson;
    private Integer totalPoints;
    private LocalDateTime applicationStartDate;
    private LocalDateTime applicationEndDate;
    private LocalDateTime announcementDate;
    private LocalDateTime experienceStartDate;
    private LocalDateTime experienceEndDate;
    private LocalDateTime reviewDate;

    public static CampaignResponse fromEntity(Campaign campaign, String imageUrl) {
        return CampaignResponse.builder()
                .id(campaign.getId())
                .businessName(campaign.getBusinessName())
                .imageUrl(imageUrl)
                .contactNumber(campaign.getContactNumber())
                .type(campaign.getType().getDisplayName())
                .category(campaign.getCategory().getDisplayName())
                .platform(campaign.getPlatform().getDisplayName())
                .label(campaign.getLabel() != null ? campaign.getLabel().getDisplayName() : null)
                .capacity(campaign.getCapacity())
                .currentApplicants(campaign.getCurrentApplicants())
                .serviceProvided(campaign.getServiceProvided())
                .requirement(campaign.getRequirement())
                .keywords(campaign.getKeywordStrings())
                .pointPayment(campaign.getPointPayment())
                .pointPerPerson(campaign.getPointPerPerson())
                .totalPoints(campaign.getTotalPoints())
                .address(campaign.getAddress())
                .postalCode(campaign.getPostalCode())
                .latitude(campaign.getLatitude())
                .longitude(campaign.getLongitude())
                .availableDays(campaign.getAvailableDayStrings())
                .applicationStartDate(campaign.getApplicationStartDate())
                .applicationEndDate(campaign.getApplicationEndDate())
                .announcementDate(campaign.getAnnouncementDate())
                .experienceStartDate(campaign.getExperienceStartDate())
                .experienceEndDate(campaign.getExperienceEndDate())
                .reviewDate(campaign.getReviewDate())
                .build();
    }
}
