package com.dain_review.domain.user.service;


import com.dain_review.domain.user.exception.UserErrorCode;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.model.request.EnterpriserChangeRequest;
import com.dain_review.domain.user.model.request.EnterpriserExtraRegisterRequest;
import com.dain_review.domain.user.model.response.EnterpriserChangeResponse;
import com.dain_review.domain.user.model.response.EnterpriserResponse;
import com.dain_review.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnterpriserService {

    private final UserRepository userRepository;

    // 사업주 회원가입 추가정보 입력
    @Transactional
    public void save(Long id, EnterpriserExtraRegisterRequest enterpriserExtraRegisterRequest) {

        // 해당 아이디의 유저 조회
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_BY_ID));

        // 선택항목 업데이트
        user.change(enterpriserExtraRegisterRequest);
    }

    // 사업주 마이페이지
    public EnterpriserResponse get(Long id) {

        // 해당 아이디의 유저 조회
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_BY_ID));

        // OwnerResponse 로 변환
        return user.toOwnerResponse();
    }

    // 사업주 회원정보 변경 페이지
    public EnterpriserChangeResponse getChange(Long id) {

        // 해당 아이디의 유저 조회
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_BY_ID));

        // OwnerChangeResponse 로 변환
        return user.toOwnerChangeResponse();
    }

    // 사업주 회원정보 변경
    @Transactional
    public EnterpriserChangeResponse change(
            EnterpriserChangeRequest enterpriserChangeRequest, Long id) {

        // 해당 아이디의 유저 조회
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_BY_ID));

        // 변경내용 업데이트
        user.change(enterpriserChangeRequest);

        // Todo - 이작업을 트랜잭션 외부로 뺄지 고민해봐야됨
        // OwnerChangeResponse 로 변환
        return user.toOwnerChangeResponse();
    }
}
