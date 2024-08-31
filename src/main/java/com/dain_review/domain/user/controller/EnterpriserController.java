package com.dain_review.domain.user.controller;


import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.domain.user.model.request.EnterpriserChangeRequest;
import com.dain_review.domain.user.model.request.EnterpriserExtraRegisterRequest;
import com.dain_review.domain.user.model.response.EnterpriserChangeResponse;
import com.dain_review.domain.user.model.response.EnterpriserResponse;
import com.dain_review.domain.user.service.EnterpriserService;
import com.dain_review.global.api.API;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enterpriser")
@RequiredArgsConstructor
public class EnterpriserController {

    private final EnterpriserService enterpriserService;

    // 사업주 회원가입 추가정보 입력
    @PostMapping("/sign-up/extra")
    public ResponseEntity register(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody EnterpriserExtraRegisterRequest enterpriserExtraRegisterRequest) {

        enterpriserService.signUpExtra(
                customUserDetails.getUserId(), enterpriserExtraRegisterRequest);

        return API.OK();
    }

    // 사업주 마이페이지
    @GetMapping("")
    public ResponseEntity<EnterpriserResponse> get(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        EnterpriserResponse enterpriserResponse =
                enterpriserService.getMyPage(customUserDetails.getUserId());

        return API.OK(enterpriserResponse);
    }

    // 사업주 회원정보 변경 페이지
    @GetMapping("/change")
    public ResponseEntity<EnterpriserChangeResponse> change(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        EnterpriserChangeResponse enterpriserChangeResponse =
                enterpriserService.getChangePage(customUserDetails.getUserId());

        return API.OK(enterpriserChangeResponse);
    }

    // 사업주 회원정보 변경
    @PatchMapping("")
    public ResponseEntity<EnterpriserChangeResponse> change(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody EnterpriserChangeRequest enterpriserChangeRequest) {

        EnterpriserChangeResponse enterpriserChangeResponse =
                enterpriserService.changeInformation(
                        enterpriserChangeRequest, customUserDetails.getUserId());

        return API.OK(enterpriserChangeResponse);
    }
}
