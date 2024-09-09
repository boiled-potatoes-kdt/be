package com.dain_review.domain.application.service;


import com.dain_review.domain.application.exception.ApplicationException;
import com.dain_review.domain.application.exception.errortype.ApplicationErrorCode;
import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.application.model.request.ApplicationRequest;
import com.dain_review.domain.application.model.response.ApplicationCampaignResponse;
import com.dain_review.domain.application.repository.ApplicationRepository;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.request.CampaignFilterRequest;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;

    public void applyCampaign(ApplicationRequest applicationRequest, Long userId) {

        User user = userRepository.getUserById(userId);
        Campaign campaign = campaignRepository.getCampaignById(applicationRequest.campaignId());

        Application application = Application.from(applicationRequest, user, campaign);
        applicationRepository.save(application);
    }

    public Page<ApplicationCampaignResponse> getApplications(
            CampaignFilterRequest campaignFilterRequest, Pageable pageable, Long userId) {

        Page<Application> applicationPage =
                applicationRepository.findByStateAndPlatformAndNameContainingAndUserId(
                        campaignFilterRequest.campaignState(),
                        campaignFilterRequest.platform(),
                        campaignFilterRequest.keyword(),
                        userId,
                        pageable);

        return applicationPage.map(ApplicationCampaignResponse::from);
    }

    @Transactional
    public void cancelApplication(Long applicationId, Long userId) {

        Application application = applicationRepository.getApplicationById(applicationId);
        CampaignState campaignState = application.getCampaign().getCampaignState();

        // 취소 가능 단계인지 확인
        // 검수증, 리뷰마감 단계이면 취소 불가
        if (CampaignState.INSPECTION.equals(campaignState)
                || CampaignState.REVIEW_CLOSED.equals(campaignState)) {
            throw new ApplicationException(ApplicationErrorCode.FAIL_CANCEL);
        }
        // 모집중 단계이면 취소가능 - 물리적 삭제
        else if (CampaignState.RECRUITING.equals(campaignState)) {
            applicationRepository.deleteByIdAndUserId(applicationId, userId);
        }
        // 모집완료, 체험&리뷰 단계이면 취소가능 - 논리적 삭제
        else {
            application.delete(userId);
        }
    }
}
