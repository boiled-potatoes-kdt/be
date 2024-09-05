package com.dain_review.domain.select.service;


import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.domain.select.model.entity.Select;
import com.dain_review.domain.select.model.request.SelectInfluencerRequest;
import com.dain_review.domain.select.repository.SelectRepository;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelectService {

    private final SelectRepository selectRepository;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;

    public void selectInfluencer(SelectInfluencerRequest selectInfluencerRequest) {
        User user = userRepository.getUserById(selectInfluencerRequest.userId());
        Campaign campaign =
                campaignRepository.getCampaignById(selectInfluencerRequest.campaignId());

        Select select = Select.from(user, campaign);
        selectRepository.save(select);
    }
}
