package com.dain_review.domain.campaign.model.response;


import com.dain_review.domain.campaign.model.entity.AvailableDay;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.Keyword;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record CampaignResponse(
        Long id,
        String businessName,
        String imageUrl,
        String contactNumber,
        String address,
        Integer postalCode,
        Double latitude,
        Double longitude,
        Set<String> availableDays,
        String type,
        String category,
        String platform,
        String label,
        Integer capacity,
        Integer currentApplicants,
        String serviceProvided,
        String requirement,
        Set<String> keywords,
        Boolean pointPayment,
        Integer pointPerPerson,
        Integer totalPoints,
        LocalDateTime applicationStartDate,
        LocalDateTime applicationEndDate,
        LocalDateTime announcementDate,
        LocalDateTime experienceStartDate,
        LocalDateTime experienceEndDate,
        LocalDateTime reviewDate) {
    public static CampaignResponse from(Campaign campaign, String imageUrl) {
        return new CampaignResponse(
                campaign.getId(),
                campaign.getBusinessName(),
                imageUrl,
                campaign.getContactNumber(),
                campaign.getAddress(),
                campaign.getPostalCode(),
                campaign.getLatitude(),
                campaign.getLongitude(),
                campaign.getAvailableDays().stream()
                        .map(AvailableDay::getDay)
                        .collect(Collectors.toSet()),
                campaign.getType().getDisplayName(),
                campaign.getCategory().getDisplayName(),
                campaign.getPlatform().getDisplayName(),
                campaign.getLabel().getDisplayName(),
                campaign.getCapacity(),
                campaign.getCurrentApplicants(),
                campaign.getServiceProvided(),
                campaign.getRequirement(),
                campaign.getKeywords().stream()
                        .map(Keyword::getKeyword)
                        .collect(Collectors.toSet()),
                campaign.getPointPayment(),
                campaign.getPointPerPerson(),
                campaign.getTotalPoints(),
                campaign.getApplicationStartDate(),
                campaign.getApplicationEndDate(),
                campaign.getAnnouncementDate(),
                campaign.getExperienceStartDate(),
                campaign.getExperienceEndDate(),
                campaign.getReviewDate());
    }
}
