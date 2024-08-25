package com.dain_review.domain.campaign.controller;


import com.dain_review.domain.campaign.model.request.CampaignFilterRequest;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
import com.dain_review.domain.campaign.service.CampaignService;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enterpriser/campaign")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    // 사업주 마이페이지에서 등록한 체험단 리스트 페이지네이션으로 가져오기
    @GetMapping
    public ResponseEntity<Page<CampaignResponse>> get(
            @ModelAttribute CampaignFilterRequest campaignFilterRequest,
            @PageableDefault(page = 1, size = 10) Pageable pageable) {

        Long userId = 2L;

        // 클라이언트한테 받은 정렬조건 무시
        Pageable pageableDefaultSorting =
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(Sort.Order.desc("id")));

        return API.OK(campaignService.get(campaignFilterRequest, pageableDefaultSorting, userId));
    }
}
