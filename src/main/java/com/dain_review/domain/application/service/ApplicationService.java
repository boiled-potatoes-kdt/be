package com.dain_review.domain.application.service;


import com.dain_review.domain.application.exception.ApplicationException;
import com.dain_review.domain.application.exception.errortype.ApplicationErrorCode;
import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.application.repository.ApplicationRepository;
import com.dain_review.domain.campaign.model.entity.enums.State;
import com.dain_review.domain.campaign.model.request.CampaignFilterRequest;
import com.dain_review.domain.campaign.model.response.ApplicationCampaignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public void save(Application application) {
        applicationRepository.save(application);
    }

    public Application getApplication(Long applicationId, Long userId) {
        return applicationRepository
                .findByIdAndUserId(applicationId, userId)
                .orElseThrow(
                        () ->
                                new ApplicationException(
                                        ApplicationErrorCode.NOT_FOUND_BY_ID_USERID));
    }

    public Page<ApplicationCampaignResponse> getApplications(
            CampaignFilterRequest campaignFilterRequest, Pageable pageable, Long userId) {
        // JPQL 쿼리를 사용하여 애플리케이션 검색 및 정렬
        return applicationRepository
                .findByStateAndPlatformAndNameContainingAndUserId(
                        campaignFilterRequest.state(),
                        campaignFilterRequest.platform(),
                        campaignFilterRequest.keyword(),
                        userId,
                        pageable // 정렬 및 페이징 정보를 직접 전달
                        )
                .map(Application::toApplicationCampaignResponse);
    }

    @Transactional
    public void cancelApplication(Long applicationId, Long userId) {
        Application application = getApplication(applicationId, userId);
        State state = application.getCampaign().getState();

        if (State.REVIEW_CLOSED.equals(state)) {
            throw new ApplicationException(ApplicationErrorCode.FAIL_CANCEL);
        } else if (State.RECRUITING.equals(state)) {
            applicationRepository.deleteById(applicationId);
        } else {
            applicationRepository.softDeleteById(applicationId);
        }
    }
}
