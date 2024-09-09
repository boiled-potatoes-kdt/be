package com.dain_review.domain.select.controller;


import com.dain_review.domain.select.model.request.SelectInfluencerRequest;
import com.dain_review.domain.select.service.SelectService;
import com.dain_review.global.api.API;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enterpriser/select")
public class SelectController {

    private final SelectService selectService;

    // 체험단을 수행할 인플루언서 선정
    @PostMapping
    public ResponseEntity selectInfluencer(
            @RequestBody @Valid SelectInfluencerRequest selectInfluencerRequest) {

        selectService.selectInfluencer(selectInfluencerRequest);
        return API.OK();
    }
}
