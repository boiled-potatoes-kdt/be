package com.dain_review.domain.choice.service;


import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.domain.choice.model.entity.Choice;
import com.dain_review.domain.choice.model.request.ChoiceInfluencerRequest;
import com.dain_review.domain.choice.repository.ChoiceRepository;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChoiceService {

    private final ChoiceRepository choiceRepository;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;

    public void choiceInfluencer(ChoiceInfluencerRequest choiceInfluencerRequest) {
        User user = userRepository.getUserById(choiceInfluencerRequest.userId());
        Campaign campaign =
                campaignRepository.getCampaignById(choiceInfluencerRequest.campaignId());

        Choice choice = Choice.from(user, campaign);
        choiceRepository.save(choice);
    }
}
