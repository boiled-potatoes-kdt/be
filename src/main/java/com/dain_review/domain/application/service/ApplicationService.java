package com.dain_review.domain.application.service;


import com.dain_review.domain.application.exception.ApplicationErrorCode;
import com.dain_review.domain.application.exception.ApplicationException;
import com.dain_review.domain.application.model.entity.Application;
import com.dain_review.domain.application.repository.ApplicationRepository;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.model.request.CampaignFilterRequest;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public void save(Application application) {

        Application savedApplication = applicationRepository.save(application);
    }

    public Application getApplication(Long applicationId, Long userId) {

        return applicationRepository
                .findByIdAndUserId(applicationId, userId)
                .orElseThrow(
                        () ->
                                new ApplicationException(
                                        ApplicationErrorCode.NOT_FOUND_BY_ID_USERID));
    }

    public Page<CampaignResponse> get(
            CampaignFilterRequest campaignFilterRequest, Pageable pageable, Long userId) {

        // 필터조건으로 Page<Application> 가져오기
        Page<Application> applicationPage =
                applicationRepository.findByStateAndPlatformAndNameContainingAndUserId(
                        campaignFilterRequest.campaignState(),
                        campaignFilterRequest.platform(),
                        campaignFilterRequest.keyword(),
                        userId,
                        pageable);

        // Page<Application> -> Page<CampaignResponse> 변환
        return applicationPage.map(Application::toCampaignResponse);
    }

    @Transactional
    public void cancel(Long applicationId, Long userId) {
        // 해당 id, userId 의 Application 가져오기
        Application application = this.getApplication(applicationId, userId);

        // 취소 가능한 단계인지 검증
        CampaignState campaignState = application.getCampaign().getCampaignState();

        // 리뷰마감 단계이면 삭제 불가능
        if (CampaignState.REVIEW_CLOSED.equals(campaignState)) {
            throw new ApplicationException(ApplicationErrorCode.FAIL_CANCEL);
        }
        // 모집중 단계이면 취소가능(취소 횟수 증가 X) -> 디비에서 삭제
        else if (CampaignState.RECRUITING.equals(campaignState)) {
            applicationRepository.deleteById(applicationId);
        }
        // 체험&리뷰 단계이거나 모집완료 단계이면 취소가능(취소횟수 증가) -> is_delete = true 변경
        else {
            applicationRepository.softDeleteById(applicationId);
        }
    }
}
