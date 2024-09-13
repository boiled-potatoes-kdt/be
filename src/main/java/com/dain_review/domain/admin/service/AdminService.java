package com.dain_review.domain.admin.service;

import com.dain_review.domain.admin.model.response.AdminCampaignListResponse;
import com.dain_review.domain.campaign.model.entity.Campaign;
import com.dain_review.domain.campaign.repository.CampaignRepository;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CampaignRepository campaignRepository;

    @Transactional(readOnly = true)
    public ResponseEntity getListCampaign(int page, int size, String keyword) {

        Pageable pageable = PageRequest.of(page, size);

        if (!Objects.isNull(keyword)) {
            Page<Campaign> campaignPage = campaignRepository.findAllByBusinessName(keyword, pageable);
            return API.OK(
                    new PageImpl<>(campaignPage.stream()
                            .map(AdminCampaignListResponse::from)
                            .collect(Collectors.toList()),
                            pageable,
                            campaignPage.getTotalElements()
                    )
            );
        }

        Page<Campaign> campaignPage = campaignRepository.findAll(pageable);
        return API.OK(
                new PageImpl<>(campaignPage.stream()
                        .map(AdminCampaignListResponse::from)
                        .collect(Collectors.toList()),
                        pageable,
                        campaignPage.getTotalElements()
                )
        );
    }


}

