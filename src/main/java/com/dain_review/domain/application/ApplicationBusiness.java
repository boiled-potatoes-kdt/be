package com.dain_review.domain.application;


import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.application.model.entity.enums.State;
import com.dain_review.domain.application.model.request.ApplicationRequest;
import com.dain_review.domain.application.service.ApplicationService;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.service.CampaignService;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.service.InfluencerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationBusiness {

    private final InfluencerService influencerService;
    private final CampaignService campaignService;
    private final ApplicationService applicationService;

    public void save(ApplicationRequest applicationRequest, Long userId) {

        User user = influencerService.getUser(userId);
        Campaign campaign = campaignService.getCampaign(applicationRequest.campaignId());

        Application application =
                Application.builder()
                        .user(user)
                        .campaign(campaign)
                        .message(applicationRequest.message())
                        .state(State.PENDING)
                        .isDeleted(false)
                        .build();

        applicationService.save(application);
    }
}
