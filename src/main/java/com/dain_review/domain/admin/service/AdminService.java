package com.dain_review.domain.admin.service;


import com.dain_review.domain.admin.model.request.CampaignApprovalRequest;
import com.dain_review.domain.admin.model.response.AdminCampaignListResponse;
import com.dain_review.domain.admin.model.response.CampaignApprovalResponse;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.model.entity.enums.CampaignState;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.global.api.API;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CampaignRepository campaignRepository;

    @Transactional(readOnly = true)
    public ResponseEntity getListCampaign(int page, int size, String keyword) {

        Pageable pageable = PageRequest.of(page, size);

        if (!Objects.isNull(keyword)) {
            Page<Campaign> campaignPage =
                    campaignRepository.findAllByBusinessName(keyword, pageable);
            return API.OK(
                    new PageImpl<>(
                            campaignPage.stream()
                                    .map(AdminCampaignListResponse::from)
                                    .collect(Collectors.toList()),
                            pageable,
                            campaignPage.getTotalElements()));
        }

        Page<Campaign> campaignPage = campaignRepository.findAll(pageable);
        return API.OK(
                new PageImpl<>(
                        campaignPage.stream()
                                .map(AdminCampaignListResponse::from)
                                .collect(Collectors.toList()),
                        pageable,
                        campaignPage.getTotalElements()));
    }

    @Transactional
    public CampaignApprovalResponse approveCampaign(
            Long campaignId, CampaignApprovalRequest request) {
        Campaign campaign = campaignRepository.getCampaignById(campaignId);

        campaign.setApplicationStartDate(request.applicationStartDate());
        campaign.setApplicationEndDate(request.applicationEndDate());
        campaign.setAnnouncementDate(request.announcementDate());
        campaign.setExperienceStartDate(request.experienceStartDate());
        campaign.setExperienceEndDate(request.experienceEndDate());
        campaign.setCampaignState(CampaignState.RECRUITING); // 승인 후 상태 변경

        // 현재 시각을 승인일로 설정
        campaign.setApprovedDate(LocalDateTime.now());

        campaignRepository.save(campaign);

        // Campaign 엔티티를 DTO로 변환하여 반환
        return CampaignApprovalResponse.from(campaign);
    }
}
