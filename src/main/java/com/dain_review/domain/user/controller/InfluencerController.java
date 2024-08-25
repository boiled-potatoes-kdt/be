package com.dain_review.domain.user.controller;


import com.dain_review.domain.user.model.request.InfluencerChangeRequest;
import com.dain_review.domain.user.model.request.InfluencerExtraRegisterRequest;
import com.dain_review.domain.user.model.response.InfluencerChangeResponse;
import com.dain_review.domain.user.model.response.InfluencerResponse;
import com.dain_review.domain.user.service.InfluencerService;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/influencer")
@RequiredArgsConstructor
public class InfluencerController {

    private final InfluencerService influencerService;

    // Todo 성공 응답을 어떻게 해야할지 모르겠음
    // 인플루언서 회원가입 추가정보 입력
    @PostMapping("/sign-up/extra")
    public ResponseEntity save(
            @RequestBody InfluencerExtraRegisterRequest influencerExtraRegisterRequest) {

        // Todo 임시값
        Long id = 1L;

        influencerService.save(id, influencerExtraRegisterRequest);

        return API.OK();
    }

    // 인플루언서 마이페이지
    @GetMapping("")
    public ResponseEntity<InfluencerResponse> get() {

        // Todo 임시값
        Long id = 1L;

        InfluencerResponse influencerResponse = influencerService.get(id);

        return API.OK(influencerResponse);
    }

    // 인플루언서 회원정보 변경 페이지
    @GetMapping("/change")
    public ResponseEntity<InfluencerChangeResponse> change() {

        // Todo 임시값
        Long id = 1L;

        InfluencerChangeResponse influencerChangeResponse = influencerService.getChange(id);

        return API.OK(influencerChangeResponse);
    }

    // 인플루언서 회원정보 변경
    @PatchMapping("")
    public ResponseEntity<InfluencerChangeResponse> change(
            @RequestBody InfluencerChangeRequest influencerChangeRequest) {

        // Todo 임시값
        Long id = 1L;

        InfluencerChangeResponse influencerChangeResponse =
                influencerService.change(influencerChangeRequest, id);

        return API.OK(influencerChangeResponse);
    }
}
