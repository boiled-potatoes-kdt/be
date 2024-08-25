package com.dain_review.domain.user.controller;


import com.dain_review.domain.user.model.request.EnterpriserChangeRequest;
import com.dain_review.domain.user.model.request.EnterpriserExtraRegisterRequest;
import com.dain_review.domain.user.model.response.EnterpriserChangeResponse;
import com.dain_review.domain.user.model.response.EnterpriserResponse;
import com.dain_review.domain.user.service.EnterpriserService;
import com.dain_review.global.api.API;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class EnterpriserController {

    private final EnterpriserService enterpriserService;

    // Todo 성공 응답을 어떻게 해야할지 모르겠음
    // 사업주 회원가입 추가정보 입력
    @PostMapping("/sign-up/extra")
    public ResponseEntity register(
            @RequestBody @Valid EnterpriserExtraRegisterRequest enterpriserExtraRegisterRequest) {

        // Todo 임시값
        Long id = 2L;

        enterpriserService.save(id, enterpriserExtraRegisterRequest);

        return API.OK();
    }

    // 사업주 마이페이지
    @GetMapping("")
    public ResponseEntity<EnterpriserResponse> get() {

        // Todo 임시값
        Long id = 2L;

        EnterpriserResponse enterpriserResponse = enterpriserService.get(id);

        return API.OK(enterpriserResponse);
    }

    // 사업주 회원정보 변경 페이지
    @GetMapping("/change")
    public ResponseEntity<EnterpriserChangeResponse> change() {

        // Todo 임시값
        Long id = 2L;

        EnterpriserChangeResponse enterpriserChangeResponse = enterpriserService.getChange(id);

        return API.OK(enterpriserChangeResponse);
    }

    // 사업주 회원정보 변경
    @PatchMapping("")
    public ResponseEntity<EnterpriserChangeResponse> change(
            @RequestBody EnterpriserChangeRequest enterpriserChangeRequest) {

        // Todo 임시값
        Long id = 1L;

        EnterpriserChangeResponse enterpriserChangeResponse =
                enterpriserService.change(enterpriserChangeRequest, id);

        return API.OK(enterpriserChangeResponse);
    }
}
