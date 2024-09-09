package com.dain_review.domain.choice.controller;


import com.dain_review.domain.choice.model.request.ChoiceInfluencerRequest;
import com.dain_review.domain.choice.service.ChoiceService;
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
@RequestMapping("/api/enterpriser/choice")
public class ChoiceController {

    private final ChoiceService choiceService;

    // 체험단을 수행할 인플루언서 선정
    @PostMapping
    public ResponseEntity selectInfluencer(
            @RequestBody @Valid ChoiceInfluencerRequest choiceInfluencerRequest) {

        choiceService.choiceInfluencer(choiceInfluencerRequest);
        return API.OK();
    }
}
