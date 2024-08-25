package com.dain_review.domain.application.controller;


import com.dain_review.domain.application.ApplicationBusiness;
import com.dain_review.domain.application.model.request.ApplicationRequest;
import com.dain_review.domain.application.service.ApplicationService;
import com.dain_review.domain.campaign.model.request.CampaignFilterRequest;
import com.dain_review.domain.campaign.model.response.CampaignResponse;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/influencer/application")
public class ApplicationController {

    private final ApplicationBusiness applicationBusiness;
    private final ApplicationService applicationService;

    // todo - 체험단 신청 완료후 응답 어떻게 해야할지
    // 체험단 신청
    @PostMapping
    public ResponseEntity application(@RequestBody ApplicationRequest applicationRequest) {

        Long userId = 1l;

        applicationBusiness.save(applicationRequest, userId);

        return API.OK();
    }

    // todo - 체험단 신청 취소 완료후 응답 어떻게 해야할지
    // 체험단 신청 취소
    @PatchMapping("/{applicationId}")
    public ResponseEntity cancel(@PathVariable Long applicationId) {

        Long userId = 1l;

        applicationService.cancel(applicationId, userId);

        return API.OK();
    }

    // 인플루언서 마이페이지에서 신청한 체험단 리스트 페이지네이션으로 가져오기
    @GetMapping
    public ResponseEntity<Page<CampaignResponse>> get(
            @ModelAttribute CampaignFilterRequest campaignFilterRequest,
            @PageableDefault(page = 1, size = 10) Pageable pageable) {

        Long id = 1L;

        // 클라이언트한테 받은 정렬조건 무시
        Pageable pageableDefaultSorting =
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(Sort.Order.desc("id")));

        return API.OK(applicationService.get(campaignFilterRequest, pageableDefaultSorting, id));
    }
}
